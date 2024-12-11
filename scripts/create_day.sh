#!/usr/bin/env bash

set -e

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

if [ $# -ne 2 ]; then
    echo "Supply the number and the name in that order"
    exit 1
fi

NUMBER=$1
NAME=$2

FORMATTED_NUMBER=$(printf %02d "$NUMBER")
FILE_NAME="Day$FORMATTED_NUMBER"
DAY="$FILE_NAME.kt"
TEST="$FILE_NAME"'Test.kt'

cp "$SCRIPT_DIR/day_template.kt" "$SCRIPT_DIR/$DAY"
cp "$SCRIPT_DIR/test_template.kt" "$SCRIPT_DIR/$TEST"

sed -i '' "s/XX/$NUMBER/g" "$SCRIPT_DIR/$DAY"
sed -i '' "s/\$NAME/$NAME/g" "$SCRIPT_DIR/$DAY"
sed -i '' "s/XX/$NUMBER/g" "$SCRIPT_DIR/$TEST"

mv "$SCRIPT_DIR/$DAY" "$SCRIPT_DIR/../src/main/kotlin/"
mv "$SCRIPT_DIR/$TEST" "$SCRIPT_DIR/../src/test/kotlin/"
touch "$SCRIPT_DIR/../src/main/resources/$FILE_NAME.txt"
touch "$SCRIPT_DIR/../src/test/resources/$FILE_NAME.txt"

echo "Created $DAY and relevant files"
