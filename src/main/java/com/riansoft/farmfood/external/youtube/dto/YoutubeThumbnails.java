package com.riansoft.farmfood.external.youtube.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class YoutubeThumbnails {

    @JsonProperty("default")
    private YoutubeThumbnail defaultThumbnail;

    private YoutubeThumbnail medium;

    private YoutubeThumbnail high;
}
