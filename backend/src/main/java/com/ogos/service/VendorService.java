package com.ogos.service;

import com.ogos.dto.ProductResponse;
import com.ogos.dto.VendorDashboardResponse;
import com.ogos.entity.User;

import java.util.List;

public interface VendorService {

    List<ProductResponse> getVendorProducts(User vendor);

    VendorDashboardResponse getDashboard(User vendor);
}
