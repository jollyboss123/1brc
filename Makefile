setup_evaluate:
	./create_measurements.sh 1000000 \
    mv measurements.txt measurements_1B.txt \
    ln -s measurements_1B.txt measurements.txt \
    ./calculate_average_baseline.sh > measurements_1B.out

.PHONY: setup_evaluate

profile:
	jbang --javaagent=ap-loader@jvm-profiling-tools/ap-loader=start,event=cpu,file=profile_$(attempt).html -m org.jolly.onebrc.CalculateAverage_$(attempt) target/onebrc-1.0-SNAPSHOT.jar

.PHONY: profile

