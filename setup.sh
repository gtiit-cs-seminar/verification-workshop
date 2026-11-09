#!/usr/bin/env bash
# Installs OpenJML in tools/openjml and checks the other tools the workshop needs:
# a JDK 21 or newer (javac), JBMC and python3.
#   ./setup.sh --vscode   also installs OpenJML's VS Code extension (optional)
set -uo pipefail
cd "$(dirname "$0")"

OPENJML_VERSION=21.0.29
ok=true

case "$(uname -s)-$(uname -m)" in
    Darwin-arm64)  zip=openjml-macos-arm64-$OPENJML_VERSION.zip ;;
    Darwin-x86_64) zip=openjml-macos-x86_64-$OPENJML_VERSION.zip ;;
    Linux-x86_64)  zip=openjml-ubuntu-24.04-$OPENJML_VERSION.zip ;;
    Linux-aarch64) zip=openjml-ubuntu-arm64-$OPENJML_VERSION.zip
                   echo "warning: OpenJML's --esc does not work yet on Linux ARM; use an x86-64 machine for ./esc." ;;
    *) echo "Unsupported platform $(uname -s)-$(uname -m). On Windows use WSL (Ubuntu 24.04), see README."; exit 1 ;;
esac

if [[ -x tools/openjml/openjml ]] && tools/openjml/openjml --version 2>/dev/null | grep -q "$OPENJML_VERSION"; then
    echo "OpenJML $OPENJML_VERSION already installed in tools/openjml"
else
    echo "Downloading $zip (about 220 MB)..."
    url=https://github.com/OpenJML/OpenJML/releases/download/$OPENJML_VERSION/$zip
    mkdir -p tools
    # -C - resumes an interrupted download
    for attempt in 1 2 3 4 5; do
        curl -fL -C - --retry 3 -o "tools/$zip" "$url" && break
        echo "download interrupted, resuming ($attempt)..."
    done
    rm -rf tools/openjml
    unzip -q "tools/$zip" -d tools/openjml && rm "tools/$zip"
fi
if tools/openjml/openjml --version >/dev/null 2>&1; then
    echo "  ok  OpenJML: $(tools/openjml/openjml --version 2>&1 | head -1)"
else
    echo "  !!  OpenJML does not run (try deleting tools/openjml and running ./setup.sh again)"; ok=false
fi

if javac -version 2>&1 | grep -Eq 'javac (2[1-9]|[3-9][0-9])'; then
    echo "  ok  $(javac -version 2>&1)"
else
    echo "  !!  javac 21 or newer not found. Install a JDK 21+ (e.g. https://adoptium.net, 'brew install openjdk@21', 'sudo apt install openjdk-21-jdk')"; ok=false
fi

if command -v jbmc >/dev/null; then
    echo "  ok  JBMC $(jbmc --version 2>&1 | head -1)"
    # a copy of JBMC's jars in a fixed place, for VS Code's Java extension (.vscode/settings.json)
    if lib=$(JBMC_LIB= tools/find-jbmc-lib 2>/dev/null); then
        mkdir -p tools/lib
        [[ "$lib" -ef tools/lib ]] || cp "$lib/cprover-api.jar" "$lib/core-models.jar" tools/lib/
    else
        echo "  !!  JBMC's cprover-api.jar not found next to jbmc; set JBMC_LIB (see tools/find-jbmc-lib)"; ok=false
    fi
else
    echo "  !!  jbmc not found. macOS: 'brew install cbmc'. Ubuntu: the .deb from https://github.com/diffblue/cbmc/releases (it includes jbmc)."; ok=false
fi

if command -v python3 >/dev/null; then
    echo "  ok  $(python3 --version)"
else
    echo "  !!  python3 not found (./bmc uses it to print counterexamples)"; ok=false
fi

if [[ "${1:-}" == "--vscode" ]]; then
    # OpenJML's own installer: installs the extension (alpha) and sets
    # openjml.serverPath in your VS Code user settings
    tools/openjml/install-vscode-extension || ok=false
fi

if $ok; then
    echo "Ready. Try: ./esc hw/Constant.java   and   ./bmc MaxCheck.check --unwind 3 --no-unwinding-assertions"
else
    exit 1
fi
