__author__ = 'Tobias Kalmes'
import singletonmixin
import nxt.locator

class BrickConnectionManager(singletonmixin.Singleton):

    _bricks = -1

    def __init__(self, a, b=1):
        pass

    def getBricks(self):
        if BrickConnectionManager._bricks == -1:
            try:
                BrickConnectionManager._bricks = nxt.locator.find_bricks()
            except:
                print "Error getting bricks"

        return BrickConnectionManager._bricks

    #def getBrick(name):
       # for item in BrickConnectionManager._bricks:
       #     if item.