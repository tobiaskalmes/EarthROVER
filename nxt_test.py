#!/usr/bin/python
#

import nxt
import sys
import tty, termios
import nxt.locator
from nxt.sensor import *
from nxt.motor import *

print "Test"

brick = nxt.locator.find_one_brick(debug=True)
motorA = nxt.Motor(brick, PORT_A)

def getch():
    fd = sys.stdin.fileno()
    old_settings = termios.tcgetattr(fd)
    try:
        tty.setraw(fd)
        ch = sys.stdin.read(1)
    finally:
        termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
    return ch

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
