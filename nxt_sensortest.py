#!/usr/bin/python
#

import threading
import time
import nxt
import sys
import tty, termios
import nxt.locator
from nxt.sensor import *
from nxt.motor import *
from nxt.sensor.hitechnic import *

brick = nxt.locator.find_one_brick(debug=True)

#sensors
irs = IRSeekerv2(brick, PORT_1)
eopds = EOPD(brick, PORT_2)
cs = Compass(brick, PORT_3)
gs = Gyro(brick, PORT_4)

#output sensor values every n miliseconds
def output_sensors():
	print "%s" % (time.ctime(time.time()))
	irdata = irs.get_dc_values()
	print "IR Seeker(Port_1): "
	print "\tDirection: ", irdata.direction
	print "\tSensor 1: ", irdata.sensor_1
	print "\tSensor 2: ", irdata.sensor_2
	print "\tSensor 3: ", irdata.sensor_3
	print "\tSensor 4: ", irdata.sensor_4
	print "\tSensor 5: ", irdata.sensor_5
	print "\tSensor mean: ", irdata.sensor_mean
	print "EOPD(Port_2) Scaled Value: ", eopds.get_scaled_value()
	print "Compass(Port_3) Heading(from North): ", cs.get_heading()
	print "Gyro(Port_4) Rotation Speed: ", gs.get_rotation_speed()

#input function
def handle_input():
	next = "next"
	quit = "quit"
	ch = "init"
	print "Ready"
	while ch != quit :
		ch = input("quit or next")
		if ch == next :
			output_sensors()
	print "Finished"
	sys.exit()

class inputHandler(threading.Thread):
    def run(self):
        handle_input()

inputThread = inputHandler()
inputThread.start()
