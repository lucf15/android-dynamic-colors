# Maven Central release

Group: `io.github.lucf15`. All four modules share `VERSION_NAME` from the root `gradle.properties`:

- `dynamic-colors`
- `dynamic-colors-compose`
- `dynamic-colors-views`
- `dynamic-colors-databinding`

Each publication includes a release AAR, sources, Dokka documentation, POM, and Gradle metadata. The sample is not published.

## Validate locally

Use JDK 17 and Android SDK 37:

```shell
./gradlew verify
./gradlew publishToMavenLocal -PsignAllPublications=false -Dmaven.repo.local="$PWD/build/maven-local"
python3 tools/check_publication.py build/maven-local
```

To check a prospective release version without changing the development version or uploading anything:

```shell
./gradlew publishToMavenLocal -PVERSION_NAME=0.1.0 -PsignAllPublications=false -Dmaven.repo.local="$PWD/build/maven-release-check"
python3 tools/check_publication.py build/maven-release-check --version 0.1.0
```

Local unsigned publication checks artifact contents, coordinates, dependencies, and metadata. It does not check the signing key or Central credentials.

The latest framework validation passed 58 unit tests (19 core, 18 Compose, 17 Views, 4 Data Binding), library release lint, release AAR builds, formatting, sample compilation, and local artifact checks. The sample was checked on the dedicated Android 17 emulator. Three API 37 instrumentation tests passed during the earlier resource validation; they were not rerun for publishing configuration changes.

## Repository and credentials

The POM points to `https://github.com/lucf15/android-dynamic-colors`. The repository is private during review. Make it public when ready for the open-source release, or update the root POM/SCM URLs if its location changes.

Use your Central Portal account with the verified `io.github.lucf15` namespace. Configure a GitHub environment named `maven-central` with these secrets:

| Secret | Value |
| --- | --- |
| `MAVEN_CENTRAL_USERNAME` | Portal token username |
| `MAVEN_CENTRAL_PASSWORD` | Portal token password |
| `SIGNING_IN_MEMORY_KEY` | ASCII-armored private signing key |
| `SIGNING_IN_MEMORY_KEY_PASSWORD` | Key passphrase, if set |
| `SIGNING_IN_MEMORY_KEY_ID` | Optional signing key ID; useful when selecting a signing subkey |

These names match TiffRenderer's publishing configuration. Secrets must be configured for this repository/environment; naming them does not copy values from another repository. The workflow maps them to the Vanniktech plugin's Gradle properties. See the [plugin's publishing documentation](https://vanniktech.github.io/gradle-maven-publish-plugin/central/).

## Signing key

A publisher can use the same GPG release-signing key for multiple libraries. A separate project key is optional. Reusing a key avoids another expiry/backup schedule; separate keys allow independent rotation of CI signing credentials.

List local keys and show a fingerprint:

```shell
gpg --list-secret-keys --keyid-format LONG
gpg --fingerprint YOUR_KEY_ID
```

If you choose a new key, run `gpg --full-generate-key` interactively, set a passphrase and expiry, and back up the key and revocation certificate. The public key must be available to Central's signature verification. Follow [Sonatype's GPG instructions](https://central.sonatype.org/publish/requirements/gpg/) for generation and public-key distribution. Do not commit the private key or print it in CI logs.

The GPG fingerprint identifies the public key. It is unrelated to Android keystore fingerprints and does not verify the Maven namespace. No key ID or fingerprint is hardcoded in this project.

## Release

1. Finish app/device validation. Set `VERSION_NAME` to the release version, commit, and create the matching `v<version>` tag.
2. Run **Maven Central release** manually on that tag. The workflow rejects snapshots and mismatched tags, runs verification, then uses the `maven-central` environment to upload signed artifacts. Leave `publish` unchecked to stage for manual review; check it to publish after Central validation.
3. For a staged deployment, inspect it in Central Portal and publish it there.

The default uses `publishToMavenCentral` for staging. With `publish` enabled, the workflow uses `publishAndReleaseToMavenCentral`. Published versions cannot be overwritten.
