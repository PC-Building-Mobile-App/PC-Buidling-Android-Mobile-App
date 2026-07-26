package com.iti.data.components.datasource

import com.iti.data.components.model.ComponentDataModel
import com.iti.domain.components.model.SearchParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ComponentMockDataSourceImpl @Inject constructor() : ComponentDataSource {

    private val mockComponents = listOf(
        ComponentDataModel(
            id = 1,
            vendorName = "TechStore",
            category = "CPU",
            productName = "AMD Ryzen 7 7800X3D",
            productImage = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=400&auto=format&fit=crop&q=60",
            price = 18500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 2,
            vendorName = "GearHub",
            category = "CPU",
            productName = "Intel Core i7-14700K",
            productImage = "https://images.unsplash.com/photo-1591453089816-0fbb971b454c?w=400&auto=format&fit=crop&q=60",
            price = 21000.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 3,
            vendorName = "PCWorld",
            category = "CPU",
            productName = "AMD Ryzen 5 7600X",
            productImage = "https://images.unsplash.com/photo-1555680202-c86f0e12f086?w=400&auto=format&fit=crop&q=60",
            price = 11500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 4,
            vendorName = "TechStore",
            category = "CPU",
            productName = "Intel Core i5-13600K",
            productImage = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=400&auto=format&fit=crop&q=60",
            price = 14800.00,
            inStock = false
        ),
        ComponentDataModel(
            id = 5,
            vendorName = "GearHub",
            category = "GPU",
            productName = "NVIDIA GeForce RTX 4070 Super",
            productImage = "https://images.unsplash.com/photo-1591488320449-011701bb6704?w=400&auto=format&fit=crop&q=60",
            price = 32000.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 6,
            vendorName = "TechStore",
            category = "GPU",
            productName = "NVIDIA GeForce RTX 4080 Super",
            productImage = "https://images.unsplash.com/photo-1624705002806-5d72df19c3ad?w=400&auto=format&fit=crop&q=60",
            price = 54000.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 7,
            vendorName = "PCWorld",
            category = "GPU",
            productName = "AMD Radeon RX 7800 XT",
            productImage = "https://images.unsplash.com/photo-1616440347437-b1c73416efc2?w=400&auto=format&fit=crop&q=60",
            price = 26500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 8,
            vendorName = "GearHub",
            category = "GPU",
            productName = "NVIDIA GeForce RTX 4060 Ti 16GB",
            productImage = "https://images.unsplash.com/photo-1601524909162-be87252be298?w=400&auto=format&fit=crop&q=60",
            price = 22000.00,
            inStock = false
        ),
        ComponentDataModel(
            id = 9,
            vendorName = "TechStore",
            category = "MOTHERBOARD",
            productName = "ASUS ROG Strix B650-A Gaming WiFi",
            productImage = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=400&auto=format&fit=crop&q=60",
            price = 11200.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 10,
            vendorName = "PCWorld",
            category = "MOTHERBOARD",
            productName = "MSI MAG Z790 Tomahawk WiFi",
            productImage = "https://images.unsplash.com/photo-1563770660941-20978e870e26?w=400&auto=format&fit=crop&q=60",
            price = 13500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 11,
            vendorName = "GearHub",
            category = "MOTHERBOARD",
            productName = "Gigabyte B650 AORUS Elite AX",
            productImage = "https://images.unsplash.com/photo-1631553127988-3486392095f7?w=400&auto=format&fit=crop&q=60",
            price = 10500.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 12,
            vendorName = "PCWorld",
            category = "MEMORY",
            productName = "Corsair Vengeance RGB 32GB (2x16GB) DDR5-6000",
            productImage = "https://images.unsplash.com/photo-1562976540-1502c2145186?w=400&auto=format&fit=crop&q=60",
            price = 6500.00,
            inStock = false
        ),
        ComponentDataModel(
            id = 13,
            vendorName = "TechStore",
            category = "MEMORY",
            productName = "G.Skill Trident Z5 Neo RGB 32GB DDR5-6000",
            productImage = "https://images.unsplash.com/photo-1600003014755-ba31aa59c4b6?w=400&auto=format&fit=crop&q=60",
            price = 6800.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 14,
            vendorName = "GearHub",
            category = "MEMORY",
            productName = "Kingston Fury Beast 16GB (2x8GB) DDR4-3200",
            productImage = "https://images.unsplash.com/photo-1541029071515-84cc54f84dc5?w=400&auto=format&fit=crop&q=60",
            price = 2400.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 17,
            vendorName = "GearHub",
            category = "PSU",
            productName = "Corsair RM850e 80+ Gold Fully Modular",
            productImage = "https://images.unsplash.com/photo-1587202372775-e229f172b9d7?w=400&auto=format&fit=crop&q=60",
            price = 6200.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 18,
            vendorName = "TechStore",
            category = "PSU",
            productName = "MSI MPG A1000G PCIE5 1000W 80+ Gold",
            productImage = "https://images.unsplash.com/photo-1614624532983-4ce03382d63d?w=400&auto=format&fit=crop&q=60",
            price = 8900.00,
            inStock = false
        ),
        ComponentDataModel(
            id = 19,
            vendorName = "PCWorld",
            category = "CASE",
            productName = "NZXT H9 Flow Dual-Chamber ATX Mid-Tower",
            productImage = "https://images.unsplash.com/photo-1555664424-778a1e5e1b48?w=400&auto=format&fit=crop&q=60",
            price = 7800.00,
            inStock = true
        ),
        ComponentDataModel(
            id = 20,
            vendorName = "GearHub",
            category = "COOLER",
            productName = "Liquid Freezer III 360 AIO Liquid Cooler",
            productImage = "https://images.unsplash.com/photo-1625805708453-2949ff12b9ff?w=400&auto=format&fit=crop&q=60",
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

            matchQuery && matchCategory && matchPrice
        }
    }
}
