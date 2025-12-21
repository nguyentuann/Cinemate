package com.movpla.detail.streaming.quality

import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector

data class QualityOption(
    val label: String,
    val height: Int,
    val width: Int,
    val bitrate: Int
)

class QualitySelector(
    private val trackSelector: DefaultTrackSelector
) {
    
    fun getAvailableQualities(tracks: Tracks): List<QualityOption> {
        val qualities = mutableListOf<QualityOption>()
        
        for (trackGroup in tracks.groups) {
            if (trackGroup.type == androidx.media3.common.C.TRACK_TYPE_VIDEO) {
                val group = trackGroup.mediaTrackGroup
                
                for (i in 0 until group.length) {
                    val format = group.getFormat(i)
                    
                    qualities.add(
                        QualityOption(
                            label = "${format.height}p",
                            height = format.height,
                            width = format.width,
                            bitrate = format.bitrate
                        )
                    )
                }
            }
        }
        
        return qualities
            .distinctBy { it.height }
            .sortedByDescending { it.height }
    }
    
    fun selectQuality(tracks: Tracks, targetHeight: Int) {
        for (trackGroup in tracks.groups) {
            if (trackGroup.type == androidx.media3.common.C.TRACK_TYPE_VIDEO) {
                val group = trackGroup.mediaTrackGroup
                
                for (i in 0 until group.length) {
                    val format = group.getFormat(i)
                    
                    if (format.height == targetHeight) {
                        val override = TrackSelectionOverride(
                            group,
                            listOf(i)
                        )
                        
                        trackSelector.parameters = trackSelector.buildUponParameters()
                            .setOverrideForType(override)
                            .build()
                        
                        return
                    }
                }
            }
        }
    }
    
    fun selectAuto() {
        trackSelector.parameters = trackSelector.buildUponParameters()
            .clearOverrides()
            .build()
    }
    
    fun getCurrentQuality(tracks: Tracks): QualityOption? {
        for (trackGroup in tracks.groups) {
            if (trackGroup.type == androidx.media3.common.C.TRACK_TYPE_VIDEO && trackGroup.isSelected) {
                val group = trackGroup.mediaTrackGroup
                
                for (i in 0 until group.length) {
                    if (trackGroup.isTrackSelected(i)) {
                        val format = group.getFormat(i)
                        return QualityOption(
                            label = "${format.height}p",
                            height = format.height,
                            width = format.width,
                            bitrate = format.bitrate
                        )
                    }
                }
            }
        }
        return null
    }
}
