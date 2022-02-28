package com.akash.repository.custom;

import java.util.List;

import com.akash.entity.BillBook;
import com.akash.entity.GoodsReturn;
import com.akash.entity.GoodsReturnSearch;

public interface GoodsReturnCustomizedRepository {
	
	public GoodsReturn billBookToGoodsReturnMapping(BillBook billbook);
	
	List<GoodsReturn> searchPaginated(GoodsReturnSearch goodsReturnSearch,int from);
	long count(GoodsReturnSearch goodsReturnSearch);

}
