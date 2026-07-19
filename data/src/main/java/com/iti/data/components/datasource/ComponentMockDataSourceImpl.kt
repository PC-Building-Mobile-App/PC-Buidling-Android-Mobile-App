package com.iti.data.components.datasource

import com.iti.data.components.model.ComponentDataModel
import com.iti.domain.components.model.SearchParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ComponentMockDataSourceImpl @Inject constructor() : ComponentDataSource {

    private val mockComponents = listOf(
        // CPUs
        ComponentDataModel(
            id = 1,
            vendorName = "TechStore",
            category = "CPU",
            productName = "AMD Ryzen 7 7800X3D",
            productImage = "https://cdn.example.com/img/1.jpg",
            price = 18500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 2,
            vendorName = "GearHub",
            category = "CPU",
            productName = "Intel Core i7-14700K",
            productImage = "https://cdn.example.com/img/2.jpg",
            price = 21000.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 3,
            vendorName = "PCWorld",
            category = "CPU",
            productName = "AMD Ryzen 5 7600X",
            productImage = "https://cdn.example.com/img/3.jpg",
            price = 11500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 4,
            vendorName = "TechStore",
            category = "CPU",
            productName = "Intel Core i5-13600K",
            productImage = "https://cdn.example.com/img/4.jpg",
            price = 14800.00,
            inStock = false
        ),

        // GPUs
        ComponentDataModel(
            id = 5,
            vendorName = "GearHub",
            category = "GPU",
            productName = "NVIDIA GeForce RTX 4070 Super",
            productImage = "https://cdn.example.com/img/5.jpg",
            price = 32000.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 6,
            vendorName = "TechStore",
            category = "GPU",
            productName = "NVIDIA GeForce RTX 4080 Super",
            productImage = "https://cdn.example.com/img/6.jpg",
            price = 54000.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 7,
            vendorName = "PCWorld",
            category = "GPU",
            productName = "AMD Radeon RX 7800 XT",
            productImage = "https://cdn.example.com/img/7.jpg",
            price = 26500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 8,
            vendorName = "GearHub",
            category = "GPU",
            productName = "NVIDIA GeForce RTX 4060 Ti 16GB",
            productImage = "https://cdn.example.com/img/8.jpg",
            price = 22000.00,
            inStock = false
        ),

        // Motherboards
        ComponentDataModel(
            id = 9,
            vendorName = "TechStore",
            category = "MOTHERBOARD",
            productName = "ASUS ROG Strix B650-A Gaming WiFi",
            productImage = "https://cdn.example.com/img/9.jpg",
            price = 11200.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 10,
            vendorName = "PCWorld",
            category = "MOTHERBOARD",
            productName = "MSI MAG Z790 Tomahawk WiFi",
            productImage = "https://cdn.example.com/img/10.jpg",
            price = 13500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 11,
            vendorName = "GearHub",
            category = "MOTHERBOARD",
            productName = "Gigabyte B650 AORUS Elite AX",
            productImage = "https://cdn.example.com/img/11.jpg",
            price = 10500.00,
            inStock = true
        ),

        // Memory (RAM)
        ComponentDataModel(
            id = 12,
            vendorName = "PCWorld",
            category = "MEMORY",
            productName = "Corsair Vengeance RGB 32GB (2x16GB) DDR5-6000",
            productImage = "https://cdn.example.com/img/12.jpg",
            price = 6500.00,
            inStock = false
        ),
        ComponentDataModel(
            id = 13,
            vendorName = "TechStore",
            category = "MEMORY",
            productName = "G.Skill Trident Z5 Neo RGB 32GB DDR5-6000",
            productImage = "https://cdn.example.com/img/13.jpg",
            price = 6800.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 14,
            vendorName = "GearHub",
            category = "MEMORY",
            productName = "Kingston Fury Beast 16GB (2x8GB) DDR4-3200",
            productImage = "https://cdn.example.com/img/14.jpg",
            price = 2400.00,
            inStock = true
        ),

        // Storage / SSDs
        ComponentDataModel(
            id = 15,
            vendorName = "TechStore",
            category = "STORAGE",
            productName = "Samsung 990 PRO 2TB M.2 NVMe SSD",
            productImage = "https://cdn.example.com/img/15.jpg",
            price = 8500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 16,
            vendorName = "PCWorld",
            category = "STORAGE",
            productName = "Western Digital Black SN850X 1TB NVMe",
            productImage = "https://cdn.example.com/img/16.jpg",
            price = 4800.00,
            inStock = true
        ),

        // Power Supplies (PSU)
        ComponentDataModel(
            id = 17,
            vendorName = "GearHub",
            category = "PSU",
            productName = "Corsair RM850e 80+ Gold Fully Modular",
            productImage = "https://cdn.example.com/img/17.jpg",
            price = 6200.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 18,
            vendorName = "TechStore",
            category = "PSU",
            productName = "MSI MPG A1000G PCIE5 1000W 80+ Gold",
            productImage = "https://cdn.example.com/img/18.jpg",
            price = 8900.00,
            inStock = false
        ),

        // Cases & Cooling
        ComponentDataModel(
            id = 19,
            vendorName = "PCWorld",
            category = "CASE",
            productName = "NZXT H9 Flow Dual-Chamber ATX Mid-Tower",
            productImage = "https://cdn.example.com/img/19.jpg",
            price = 7800.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 20,
            vendorName = "GearHub",
            category = "COOLER",
            productName = "Liquid Freezer III 360 AIO Liquid Cooler",
            productImage = "https://cdn.example.com/img/20.jpg",
            price = 5500.00,
            inStock = true
        )
    )

    override fun getComponents(): Flow<List<ComponentDataModel>> {
        return flowOf(mockComponents)
    }

    override fun getComponentById(id: Long): Flow<ComponentDataModel?> {
        val component = mockComponents.find { it.id == id }
        return flowOf(component)
    }

    override suspend fun searchComponents(params: SearchParams): List<ComponentDataModel> {
        //todo (Implement real remote search once the backend is done)
        val query = params.query?.trim()
        val category = params.category
        val minPrice = params.minPrice
        val maxPrice = params.maxPrice

        return mockComponents.filter { component ->
            val matchQuery = query.isNullOrBlank() ||
                    component.productName.contains(query, ignoreCase = true) ||
                    component.vendorName.contains(query, ignoreCase = true)

            val matchCategory = category == null ||
                    component.category.equals(category.name, ignoreCase = true)

            val matchPrice = (minPrice == null || component.price >= minPrice) &&
                    (maxPrice == null || component.price <= maxPrice)

            val matchStock = !params.inStockOnly || component.inStock

            matchQuery && matchCategory && matchPrice && matchStock
        }
    }
}
