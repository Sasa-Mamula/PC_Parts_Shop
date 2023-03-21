package com.example.pcshop.listener;

import com.example.pcshop.model.PartsModel;

import java.util.List;

public interface IPartsLoadListener {
    void onGameLoadSuccess(List<PartsModel> gameModelList);
    void onGameLoadFailed(String message);
}
