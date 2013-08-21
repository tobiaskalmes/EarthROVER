#!/usr/bin/python
#

import threading
import time
import nxt
import sys
import tty
import termios
import nxt.locator
from nxt.sensor import *
from nxt.motor import *

#init bricks
brickL = nxt.locator.find_one_brick(host='00:16:53:0B:EB:04', debug=True)
brickR = nxt.locator.find_one_brick(host='00:16:53:0E:BF:FD', debug=True)

#motors for left side
motorsLeftSide = nxt.Motor(brickL, PORT_A)
#motorL2 = nxt.Motor(brickL, PORT_B)

#motors for right side
motorsRightSide = nxt.Motor(brickR, PORT_A)
#motorR2 = nxt.Motor(brickR, PORT_B)

speedL = 100
speedR = 100
degrees = 720

class turnMotorsLeftSide(threading.Thread):
	def run(self):
		motorsLeft.turn(speedL, degrees, False)

class turnMotorsRightSide(threading.Thread):
	def run(self):
		motorsRightSide.turn(speedR, degrees, False)

#handle input
def handle_input():
	ch = " "
	print "Ready"
	while ch != "q" :
		ch = raw_input("Waiting for input:")
		if ch == "w" :
			print "Forward"
			#motorsLeftSide.turn(100, 720, False)
			#motorL2.turn(-100, 360, False)
			#motorsRightSide.turn(100, 720, False)
			#motorR2.turn(-100, 360, False)
			speedL = 100
			speedR = 100
			left = turnMotorsLeftSide()
			right = turnMotorsRightSide()
			left.start()		
			right.start()

		if ch == "s" :
			print "Backwards"
			motorsLeftSide.turn(-100, 720, False)
			#motorL2.turn(100, 360, False)
			motorsRightSide.turn(-100, 720, False)
			#motorR2.turn(100, 360, False)
        
		if ch == "a" :
			print "Spin Left"
			motorsLeftSide.turn(-100, 720, False)
			#motorL2.turn(-100, 360, False)
			motorsRightSide.turn(100, 720, False)
			#motorR2.turn(-100, 360, False)
		
		if ch == "d" :
			print "Spin Right"
			motorsRightSide.turn(100, 720, False)
			#motorL2.turn(-100, 360, False)
			motorsLeftSide.turn(-100, 720, False)
			#motorR2.turn(100, 360, False)
	print "Finished"
	sys.exit()

class inputHandler(threading.Thread):
	def run(self):
		handle_input()

inputThread = inputHandler()

inputThread.start()
