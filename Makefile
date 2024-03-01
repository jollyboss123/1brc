setup_evaluate:
	./create_measurements.sh 1000000 \
    mv measurements.txt measurements_1B.txt \
    ln -s measurements_1B.txt measurements.txt \
    ./calculate_average_baseline.sh > measurements_1B.out

.PHONY: setup_evaluate

