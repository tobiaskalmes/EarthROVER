__author__ = 'Tobias Kalmes'

import BrickConnectionManager

bcm = BrickConnectionManager.BrickConnectionManager.getInstance()
bricks = bcm.getBricks()

for (i, item) in enumerate(bricks):
    print "Item", i, ":", item
