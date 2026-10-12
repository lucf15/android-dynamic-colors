"""Reject snapshot, branch, and mismatched-tag Central releases. No network access."""
import re
import sys
from pathlib import Path

root = Path(__file__).resolve().parents[1]
properties = dict(
    line.split("=", 1)
    for line in (root / "gradle.properties").read_text().splitlines()
    if "=" in line and not line.startswith("#")
)
version = properties["VERSION_NAME"]
if not re.fullmatch(r"[0-9]+\.[0-9]+\.[0-9]+(?:-(?:alpha|beta|rc)[0-9]+)?", version):
    sys.exit(f"Release requires a non-snapshot version; found {version}")
if len(sys.argv) != 2 or sys.argv[1] != f"refs/tags/v{version}":
    sys.exit(f"Run this workflow on tag v{version}, matching VERSION_NAME")
print(f"Release guard passed for {version}")
