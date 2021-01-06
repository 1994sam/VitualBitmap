# Vitual Bitmap
This repository has the implementation of the Virtual Bitmap data structure. 
It is a data structure that can be used for the estimation of number of flows in a network. 

To map a flow in the the bitmap, we use a hash function on the flow ID.  At the beginning of the measurement interval all bits of a Virtual bitmap are set to zero. Whenever a packet comes in, the bit its flow ID hashes to is set to 1. Note that all packets belonging to the same flow map to the same bit, so each flow turns on at most one bit irrespective of the number of packets it sends.
We then use these set bit to estimate the number of flows in a network.
