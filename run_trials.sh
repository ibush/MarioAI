cd bin

rm stats/*
rm params/*

java -cp .:../lib/* ch.idsia.scenarios.Main -ag cs221.NNAgent -vis off
