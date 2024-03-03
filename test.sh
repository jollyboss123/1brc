#!/bin/bash

set -euo pipefail

DEFAULT_INPUT="src/test/resources/samples/*.txt"
FORK=${1:-""}
INPUT=${2:-$DEFAULT_INPUT}

if [ "$#" -eq 0 ] || [ "$#" -gt 2 ] || [ "$FORK" = "-h" ]; then
  echo "Usage: ./test.sh <fork name> [input file pattern]"
  echo
  echo "For each test sample matching <input file pattern> (default '$DEFAULT_INPUT')"
  echo "runs <fork name> implementation and diffs the result with the expected output."
  echo "Note that optional <input file pattern> should be quoted if contains wild cards."
  echo
  echo "Examples:"
  echo "./test.sh baseline_stream"
  echo "./test.sh baseline_stream src/test/resources/samples/measurements-1.txt"
  echo "./test.sh baseline_stream 'src/test/resources/samples/measurements-*.txt'"
  exit 1
fi

if [ -f "./prepare_$FORK.sh" ]; then
  "./prepare_$FORK.sh"
fi

for sample in $(ls $INPUT); do
  echo "Validating calculate_average_$FORK.sh -- $sample"

  rm -f measurements.txt
  ln -s $sample measurements.txt

  diff --color=always <("./calculate_average_$FORK.sh" | ./tocsv.sh) <(./tocsv.sh < ${sample%.txt}.out)
done

rm measurements.txt
