#!/usr/bin/php -q
<?php
	# Use: php TestStopWatch.php
	# testing the stop watch class
	include_once "StopWatchClass.php";

	# create a stopwatch object
	$watch = new stopwatch;

	# restart the stop watch in milliseconds with a new name
	$watch->Start('testing', false);

	# sleep 1 second
	sleep(1);

	# note the end of the interval
	$watch->Interval('1 second later');

	# sleep 2 seconds
	sleep(2);

	# note the end of the interval
	$watch->Interval("2 seconds later");

	# loop doing nothing 100,000 times
	for ($i = 0; $i <= 100000; ++$i) { ;}

	# note the end of the loop
	$watch->Interval("for-loop done");

	# get the latest interval
	echo "{$watch->GetNamedInterval()}\n";

	# get just one interval by name
	echo "{$watch->GetNamedInterval('1 second later')}\n";

	# get another interval by name
	echo "{$watch->GetNamedInterval('2 seconds later')}\n";

	# get a result for an unknown (error) interval
	echo "{$watch->GetNamedInterval('junk')}\n";

	# enter an un-named interval, time for the 4 prior echo statements
	$watch->Interval();

	# print results of all intervals so far
	print_r($watch->AllIntervals());

	# wait another second
	sleep(1);

	# print_r the results of all intervals
	print_r($watch->Stop('last'));

# end script
?>
