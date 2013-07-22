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

brick = nxt.locator.find_one_brick(debug=True)
motorA = nxt.Motor(brick, PORT_A)

#sensors
touch_sensor = Touch(brick, PORT_1)
light_sensor = Light(brick, PORT_2)
sound_sensor = Sound(brick, PORT_3)
ultrasonic_sensor = Ultrasonic(brick, PORT_4)

#output sensor values every n miliseconds
def output_sensors(delay):
    print "%s" % (time.ctime(time.time()))
    print "Touch(Port_1): ", touch_sensor.get_sample()
    print "Light(Port_2): ", light_sensor.get_sample()
    print "Sound(Port_3): ", sound_sensor.get_sample()
    print "Ultrasonic(Port_4): ", ultrasonic_sensor.get_sample()
    time.sleep(delay)

class outputSensors(threading.Thread):
    def run(self):
        while 1:
            output_sensors(1)
        
#read key
def getch():
    fd = sys.stdin.fileno()
    old_settings = termios.tcgetattr(fd)
    try:
        tty.setraw(fd)
        ch = sys.stdin.read(10)
    finally:
        termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
    return ch

#input function
def handle_input():
    ch = ' '
    print "Ready"
    while ch != 'q' :
        ch = getch()
        if ch == 'w' :
            print "Forwards"
            motorA.turn(100, 360, False)
        elif ch == 's' :
            print "Backwards"
            motorA.turn(-100, 360, False)
    print "Finished"
    sys.exit()

class inputHandler(threading.Thread):
    def run(self):
        handle_input()

sensorThread = outputSensors()
inputThread = inputHandler()

sensorThread.start()
#time.sleep(1000)
inputThread.start()
