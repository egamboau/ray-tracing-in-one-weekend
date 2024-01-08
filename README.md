Ray Tracing in One Weekend - Java Version
=========================================

This repository implements the code for a basic ray tracing, as described in the book *Ray Tracing in One Weekend* by the authors [Peter Shirley](https://github.com/petershirley), [Trevor David Black](https://github.com/trevordblack), [Steve Hollasch](https://github.com/hollasch)

The book can be read online, for free, in the following [here](https://raytracing.github.io/books/RayTracingInOneWeekend.html).

The code implementation will try to be very similar to the one in the book, however, a couple of things will be changed:
1. The rendered image will be shown in a canvas, instead of a PPM file. The image will not be printed as a file, like in the book.
2. Sometimes, the book references to zooming in and out the generated images. As this program will not write any file, a zoom feature was implemented:

    1. Use the plus and minus keys on your keyword to zoom in and out.
    2. Use the arrows to scroll the images if the image is zoomed in. 

How this repo will be structured
--------------------------------
The main branch will include the latest code. The main branch will be tagged, so is easier to see how the code evolves as following the sections of the book. Numeration for the tags will follow the chapters/sections from the book on the link. 

Note that this code is based on version _4.0.0-alpha.1_, released on 2023-08-06, Other future versions may change the numbering formats. 