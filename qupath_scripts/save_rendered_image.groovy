/**
 * Authors: Antoine A. Ruzette, Simon F. Nørrelykke
 * Date: 2024-03-23
 *
 * This script saves a rendered image of the current viewer to a file.
 * The default output directory is the QuPath project's root directory.
 * 
 * Released under the MIT License (see LICENSE file)
 */


import qupath.imagej.tools.IJTools
import qupath.lib.gui.images.servers.RenderedImageServer
import qupath.lib.gui.viewer.overlays.HierarchyOverlay
import qupath.lib.regions.RegionRequest

import static qupath.lib.gui.scripting.QPEx.*

// It is important to define the downsample!
// This is required to determine annotation line thicknesses
double downsample = 20

// Add the output file path here
String path = buildFilePath(PROJECT_BASE_DIR, 'rendered', getProjectEntry().getImageName() + '.png')

// Request the current viewer for settings, and current image (which may be used in batch processing)
def viewer = getCurrentViewer()
def imageData = getCurrentImageData()

// Create a rendered server that includes a hierarchy overlay using the current display settings
def server = new RenderedImageServer.Builder(imageData)
    .downsamples(downsample)
    .layers(new HierarchyOverlay(viewer.getImageRegionStore(), viewer.getOverlayOptions(), imageData))
    .build()

// Write or display the rendered image
if (path != null) {
    mkdirs(new File(path).getParent())
    writeImage(server, path)
} else
    IJTools.convertToImagePlus(server, RegionRequest.createInstance(server)).getImage().show()