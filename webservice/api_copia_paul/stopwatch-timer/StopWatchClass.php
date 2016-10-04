<?php
/*

	2/25/2014

	Useful in determining time to complete various tasks inside a php script.
	Keeps track of time intervals in either milliseconds (by default) or microseconds from the Start to the Stop
	with an unlimited number of intermediate, named intervals.

	Start, stop, and interval entries may be given optional unique names.
	When a start, stop, or interval name is not given, the class assigns a name beginning with ~.
	To run the stop watch you need, as a minimum, to create the object and then call the Stop function.
	You can create intervals anywhere the stopwatch object is visible in your script.
	Individual intervals can be retrieved by name.
	The array of intervals can be retrieved at any time.
	Stop automatically returns the entire interval array.
	The increments between intervals are saved in a name-value-pair (NvP) array
	and are not deleted when the timer stops, so intervals and the entire array can be retrieved
	after the timer is stopped and before another start is issued.

	You need to have the following in your script.
	include_once [path.] "StopWatchClass.php";
	$watch = new stopwatch();

	Copyright (C) 2014 Software Installation Services, Inc.
	Author: Bob Wedwick, Phoenix, AZ 602-449-8552  bob at wedwick dot com.

	This program is free software: you can redistribute it or modify it under the terms
	of the GNU General Public License as published by the Free Software Foundation,
	either version 3 of the License or any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	See http://www.gnu.org/licenses/licenses.en.html for a copy of the GNU General Public License.

	An example test php script for the stop watch class is in the script named TestStopWatch.php.
	The output for the test script is in TestStopWatchOutput.txt
	Script logic in the form of pseudocode is in StopWatchClass.php.pseudo.txt
*/

### class name is stopwatch
class stopwatch {

	# initialize properties
	# NvP array of time intervals
	private $intervals = array();

	# elapsed microtime for the latest entry
	private $lastInterval = 0;

	# microtime when the stopwatch started
	private $startTime = false;

	private $microsec = false;

### constructor
function __construct() {

	# start the timer by default
	$this->Start();
# end function
}

### make one entry with an ID name in the interval array - AddInterval($id, $oneTime= 0)
private function AddInterval($id, $oneTime= 0) {

	# enter the time for the named interval
	$this->intervals[$id] = $oneTime;

# end function
}

### return all the intervals so far - AllIntervals()
function AllIntervals() {

	# return the complete NvP interval array
	return $this->intervals;

# end function
}

### get the time for one named interval or the latest interval - GetNamedInterval($id = null)
function GetNamedInterval($id = null) {

	# if $id name is null
	if ($id === null) {

		# return the latest interval
		foreach($this->intervals as $id =>$val) ;
		$ret = "$id = $val";

	# else if there is a $id name
	} elseif (array_key_exists($id, $this->intervals))

		# return the named interval
		$ret = "$id = {$this->intervals[$id]}";

	# else return 'no value'
	else $ret = "$id = no value";

	return $ret;
# end function
}

### record the duration for one interval - Interval($id = null)
function Interval($id = null) {

	# get the current microtime
	$now = microtime(true);

	# if no $id name is given for the interval
	if ($id == null) {

		# give it a counter interval name
		$cnt = count($this->intervals) ;
		$cnt++;
		$id = "~interval $cnt";

	# ....
	}

	# if starting the stop watch
	if ($this->startTime == false) {

		# set start time interval to zero
		$incr = 0;

		# note the starting time
		$this->startTime = $now;

	# else
	} else {

		# compute time since the prior interval
		$incr = $now - $this->lastInterval;

		# if calling for microseconds round the time to microseconds otherwise to milliseconds
		$incr = ($this->microsec)
			? round($incr, 6)
			: round($incr, 3);
	# ....
	}

	# add it to the interval array
	$this->AddInterval($id, $incr);

	# update the last interval time
	$this->lastInterval  = $now;

# end function
}

### start or restart the interval timer - Start($id = null, $microseconds=false )
function Start($id = null, $microseconds=false ) {

	# reset the interval array
	$this->intervals = array();

	# if no $id name is sent call it '~start'
	if ($id == null) $id = '~start';

	# set or clear the microsecond flag
	$this->microsec = $microseconds;

	# make the starting entry in the interval array
	$this->startTime = false;
	$this->Interval($id);

# end function
}

### stop timing and return the interval array - Stop($id = null)
function Stop($id = null) {

	# if no $id name is sent call it '~stopped'
	if ($id == null) $id = '~stopped';

	# enter a stop interval time
	$this->Interval($id);

	# sum all intervals
	$tot = 0;
	foreach ($this->intervals as $t) $tot += $t;

	# make a total time entry in the array
	$this->AddInterval("~total time", $tot);

	# return the complete array
	return $this->AllIntervals();

# end function
}

# end class
}

?>