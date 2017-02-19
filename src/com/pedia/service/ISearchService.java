package com.pedia.service;

import java.util.List;

import com.pedia.tool.EntryInfo;

public interface ISearchService {
	
	List<EntryInfo> search(String key);
}
