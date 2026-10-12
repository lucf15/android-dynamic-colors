"""Validate the locally published Maven artifacts without uploading them."""
import argparse
import json
import xml.etree.ElementTree as ET
from pathlib import Path
from zipfile import ZipFile

root = Path(__file__).resolve().parents[1]
version = next(line.split("=", 1)[1] for line in (root / "gradle.properties").read_text().splitlines() if line.startswith("VERSION_NAME="))
parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument("repository", type=Path)
parser.add_argument("--version", default=version, help="Version passed to Gradle as -PVERSION_NAME")
args = parser.parse_args()
repository = args.repository
version = args.version
ns = {"m": "http://maven.apache.org/POM/4.0.0"}
expected = {
    "dynamic-colors": {"androidx.annotation", "org.jetbrains.kotlin"},
    "dynamic-colors-views": {"io.github.lucf15", "org.jetbrains.kotlin", "org.jetbrains.kotlinx"},
    "dynamic-colors-databinding": {"io.github.lucf15", "org.jetbrains.kotlin", "androidx.databinding"},
    "dynamic-colors-compose": {"io.github.lucf15", "org.jetbrains.kotlin", "androidx.compose.runtime", "androidx.compose.ui", "androidx.compose.foundation"},
}
for artifact, groups in expected.items():
    directory = repository / "io/github/lucf15" / artifact / version
    stem = directory / f"{artifact}-{version}"
    pom = ET.parse(str(stem) + ".pom").getroot()
    for tag, value in [("groupId", "io.github.lucf15"), ("artifactId", artifact), ("version", version), ("packaging", "aar")]:
        assert pom.findtext(f"m:{tag}", namespaces=ns) == value, (artifact, tag)
    for tag in ["name", "description", "url", "licenses/m:license/m:name", "developers/m:developer/m:id", "scm/m:url"]:
        assert pom.findtext(f"m:{tag}", namespaces=ns), (artifact, tag)
    for tag, value in [
        ("url", "https://github.com/lucf15/android-dynamic-colors"),
        ("developers/m:developer/m:id", "lucf15"),
        ("licenses/m:license/m:url", "https://www.apache.org/licenses/LICENSE-2.0.txt"),
    ]:
        assert pom.findtext(f"m:{tag}", namespaces=ns) == value, (artifact, tag)
    dependencies = pom.findall("m:dependencies/m:dependency", ns)
    for dependency in dependencies:
        if dependency.findtext("m:groupId", namespaces=ns) == "io.github.lucf15":
            assert dependency.findtext("m:version", namespaces=ns) == version, (artifact, "module version mismatch")
    actual = {dep.findtext("m:groupId", namespaces=ns) for dep in dependencies}
    assert actual == groups, (artifact, actual)
    metadata = json.loads(Path(str(stem) + ".module").read_text())
    for variant in metadata["variants"]:
        for dep in variant.get("dependencies", []):
            assert dep["group"] in groups | ({"androidx.compose"} if artifact.endswith("compose") else set()), (artifact, dep)
    for suffix in ["-sources.jar", "-javadoc.jar"]:
        with ZipFile(str(stem) + suffix) as archive:
            assert any(name.endswith(".kt" if "sources" in suffix else ".html") for name in archive.namelist()), (artifact, suffix)
    with ZipFile(str(stem) + ".aar") as archive:
        assert "classes.jar" in archive.namelist()
        if artifact == "dynamic-colors-databinding":
            assert any(name.endswith("-setter_store.json") for name in archive.namelist()), "Missing exported binding adapters"
        aar_metadata = archive.read("META-INF/com/android/build/gradle/aar-metadata.properties").decode()
        minimum = 37 if artifact.endswith("compose") else 21
        assert f"minCompileSdk={minimum}\n" in aar_metadata, (artifact, aar_metadata)
    print(f"Validated {artifact}: AAR, sources, docs, POM, metadata, and dependency boundaries")
