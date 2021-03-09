package com.manuelblanco.opendemo.common

/**
 * Enum for the different sizes of thumbnails in Marvel API.
 */
enum class ThumbnailSize(val size: String) {
    SMALL("/portrait_small"), MEDIUM("/portrait_medium"), XLARGE("/portrait_xlarge"),
    FANTASTIC("/portrait_fantastic"), UNCANNY("/portrait_uncanny"), INCREDIBLE("/portrait_incredible")
}