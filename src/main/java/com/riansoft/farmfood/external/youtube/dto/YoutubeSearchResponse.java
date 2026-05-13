package com.riansoft.farmfood.external.youtube.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class YoutubeSearchResponse {

    private List<YoutubeSearchItem> items;
}
