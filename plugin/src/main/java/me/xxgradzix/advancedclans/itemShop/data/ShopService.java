package me.xxgradzix.advancedclans.itemShop.data;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopPlayerBalanceEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductPriceType;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopCategoryRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopPlayerBalanceEntityRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopProductEntityRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopProductPriceTypeRepository;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.*;

public class ShopService {

    private final ItemShopProductEntityRepository productEntityManager;

    private final ItemShopCategoryRepository categoryRepository;
    private final ItemShopPlayerBalanceEntityRepository playerBalanceEntityManager;
    private final ItemShopProductPriceTypeRepository itemShopProductPriceTypeRepository;

    private static final HashMap<String, ItemShopCategoryEntity> cachedCategories = new HashMap<>();
    private static final HashMap<ItemShopCategoryEntity, List<ItemShopProductEntity>> cachedProducts = new HashMap<>();

    private static final HashMap<ItemShopProductEntity, Map<ShopType, ItemShopProductPriceType>> cachedPrices = new HashMap<>();

    private static final boolean useDB = true;

    public ShopService(ItemShopProductEntityRepository productEntityManager, ItemShopCategoryRepository categoryRepository, ItemShopPlayerBalanceEntityRepository playerBalanceEntityManager, ItemShopProductPriceTypeRepository itemShopProductPriceTypeRepository) {
        this.productEntityManager = productEntityManager;
        this.categoryRepository = categoryRepository;
        this.playerBalanceEntityManager = playerBalanceEntityManager;
        this.itemShopProductPriceTypeRepository = itemShopProductPriceTypeRepository;

        loadAllCategories(categoryRepository);
        loadAllProducts(productEntityManager);
        loadAllPrices(itemShopProductPriceTypeRepository);
    }

    /** PRICES **/

    public void setPriceToProduct(ItemShopProductEntity product, ShopType shopType, int price) {

        if(!useDB) return;

        Optional<ItemShopProductPriceType> optionalPriceType = getPriceType(product, shopType);

        ItemShopProductPriceType priceType;

        if(optionalPriceType.isPresent()) {
            priceType = optionalPriceType.get();
            priceType.setPrice(price);
        } else {
            priceType = new ItemShopProductPriceType(product, shopType, price);
        }

        try {

            itemShopProductPriceTypeRepository.createOrUpdateEntity(priceType);
            Map<ShopType, ItemShopProductPriceType> prices = cachedPrices.getOrDefault(product, new HashMap<>());
            prices.put(shopType, priceType);
            cachedPrices.put(product, prices);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public Optional<ItemShopProductPriceType> getPriceType(ItemShopProductEntity product, ShopType shopType) {

        if(cachedPrices.containsKey(product)) {
            return Optional.ofNullable(cachedPrices.getOrDefault(product, new HashMap<>()).get(shopType));
        }

        if(!useDB) return Optional.empty();
        try {
            List<ItemShopProductPriceType> productId = itemShopProductPriceTypeRepository.getAllEntitiesByFields(Map.of("product_id", product.getId(), "shopType", shopType));
            return Optional.ofNullable(productId.getFirst());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<ShopType, ItemShopProductPriceType> getProductPrices(ItemShopProductEntity product) {

        if(cachedPrices.containsKey(product)) {
            return cachedPrices.getOrDefault(product, new HashMap<>());
        }


        if(!useDB) return new HashMap<>();
        try {
            List<ItemShopProductPriceType> products = itemShopProductPriceTypeRepository.getAllEntitiesByField("product_id", product.getId());
            Map<ShopType, ItemShopProductPriceType> prices = new HashMap<>();
            for (ItemShopProductPriceType productPriceType : products) {
                prices.put(productPriceType.getShopType(), productPriceType);
            }
            return prices;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemShopProductPriceType deletePriceType(ItemShopProductEntity product, ShopType shopType) {
        if(!useDB) return null;
        Optional<ItemShopProductPriceType> priceType = getPriceType(product, shopType);

        if (priceType.isEmpty()) return null;

        ItemShopProductPriceType priceTypeEntity = priceType.get();

        try {
            ItemShopProductPriceType priceType1 = itemShopProductPriceTypeRepository.deleteEntity(priceTypeEntity);
            if(cachedPrices.containsKey(product)) {
                Map<ShopType, ItemShopProductPriceType> prices = cachedPrices.get(product);
                prices.remove(shopType);
                cachedPrices.put(product, prices);
            }
            return priceType1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** PRODUCTS **/

    public void deleteProduct(ItemShopProductEntity entity) {
        if(!useDB) return;

        Map<ShopType, ItemShopProductPriceType> productPrices = getProductPrices(entity);

        for (ItemShopProductPriceType productPrice : productPrices.values()) {
            try {
                itemShopProductPriceTypeRepository.deleteEntity(productPrice);

                cachedPrices.get(entity).remove(productPrice.getShopType());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            productEntityManager.deleteEntity(entity);

            List<ItemShopProductEntity> orDefault = cachedProducts.getOrDefault(entity.getCategory(), new ArrayList<>());
            orDefault.remove(entity);
            cachedProducts.put(entity.getCategory(), orDefault);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void createProduct(ItemShopCategoryEntity categoryEntity, ItemStack item, int slot) {
        if(!useDB) return;

        ItemShopCategoryEntity category;

        try {
            category = categoryRepository.getEntityById(categoryEntity.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(item == null || Material.AIR.equals(item.getType())) throw new IllegalArgumentException("ItemStack cant be null or air");

        ItemShopProductEntity entity = new ItemShopProductEntity(item, slot, category);

        createOrUpdateProduct(entity);
    }

    public void createOrUpdateProduct(ItemShopProductEntity product) {
        if(!useDB) return;

        if(product.getProduct() == null || Material.AIR.equals(product.getProduct().getType())) throw new IllegalArgumentException("ItemStack cant be null or air");

        try {
            productEntityManager.createOrUpdateEntity(product);

            List<ItemShopProductEntity> orDefault = cachedProducts.getOrDefault(product.getCategory(), new ArrayList<>());
            orDefault.add(product);
            cachedProducts.put(product.getCategory(), orDefault);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ItemShopProductEntity> getProductsByCategory(ItemShopCategoryEntity category) {

        if(cachedProducts.containsKey(category)) {
            return cachedProducts.get(category);
        }

        try {
            return productEntityManager.getAllEntitiesByField("category_id", category.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** CATEGORIES **/

    public void createCategory(String name, ItemStack categoryIcon, int categorySlot) {
        if(!useDB) return;

        ItemShopCategoryEntity entity = new ItemShopCategoryEntity(name, categorySlot, categoryIcon);

        createOrUpdateCategory(entity);
    }

    public void createOrUpdateCategory(ItemShopCategoryEntity category) {
        if(!useDB) return;

        try {
            categoryRepository.createOrUpdateEntity(category);

            cachedCategories.put(category.getName(), category);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ItemShopCategoryEntity> getAllCategories() {

        return new ArrayList<>(cachedCategories.values());
    }

    public Optional<ItemShopCategoryEntity> getCategoryByName(String name) {
        if(cachedCategories.containsKey(name)) {
            return Optional.ofNullable(cachedCategories.get(name));
        }
        try {
            return Optional.ofNullable(categoryRepository.getAllEntitiesByField("name", name).getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /** BALANCE **/

    public boolean subtractMoneyFromPlayer(Player player, ShopType shopType, int price) {

        ItemShopPlayerBalanceEntity entity = getPlayerBalanceEntity(player);

        switch (shopType) {
            case TIME:
            {
                int currentCoins = entity.getTimeCoins();
                if(currentCoins - price >= 0) {
                    entity.setTimeCoins((currentCoins-price));
                    try {
                        playerBalanceEntityManager.createOrUpdateEntity(entity);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
                return false;
            }
            case KILLS:
            {
                int currentCoins = entity.getKillCoins();
                if(currentCoins - price >= 0) {
                    entity.setKillCoins((currentCoins-price));
                    try {
                        playerBalanceEntityManager.createOrUpdateEntity(entity);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
                return false;
            }
            case MONEY:
            {
                return AdvancedGuilds.getEconomy().withdrawPlayer(player, price).transactionSuccess();
            }
        }
        return false;
    }

    public ItemShopPlayerBalanceEntity getPlayerBalanceEntity(Player player) {
        Optional<ItemShopPlayerBalanceEntity> optional;
        try {
            optional = Optional.ofNullable(playerBalanceEntityManager.getEntityById(player.getUniqueId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ItemShopPlayerBalanceEntity entity;
        if(optional.isPresent()) {
            entity = optional.get();
        } else {
            entity = new ItemShopPlayerBalanceEntity(player.getUniqueId(), 0, 0);
            try {
                playerBalanceEntityManager.createOrUpdateEntity(entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return entity;
    }

    public boolean canAfford(Player player, ShopType shopType, int price) {
        ItemShopPlayerBalanceEntity entity = getPlayerBalanceEntity(player);

        return switch (shopType) {
            case TIME -> entity.getTimeCoins() - price < 0;
            case KILLS -> (entity.getKillCoins() - price < 0);
            case MONEY -> (AdvancedGuilds.getEconomy().getBalance(player) - price < 0);
        };
    }

    public void createPlayerBalanceEntityIfNotExists(Player player) {
        Optional<ItemShopPlayerBalanceEntity> optional;
        try {
            optional = Optional.ofNullable(playerBalanceEntityManager.getEntityById(player.getUniqueId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(optional.isEmpty()) {
            ItemShopPlayerBalanceEntity entity = new ItemShopPlayerBalanceEntity(player.getUniqueId(), 0, 0);
            try {
                playerBalanceEntityManager.createOrUpdateEntity(entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean addMoneyToPlayer(Player player, ShopType shopType, int price) {
        ItemShopPlayerBalanceEntity entity = getPlayerBalanceEntity(player);

        switch (shopType) {
            case TIME:
            {
                int currentCoins = entity.getTimeCoins();
                entity.setTimeCoins(currentCoins + price);
                try {
                    playerBalanceEntityManager.createOrUpdateEntity(entity);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
            case KILLS:
            {
                int currentCoins = entity.getKillCoins();
                entity.setKillCoins(currentCoins + price);
                try {
                    playerBalanceEntityManager.createOrUpdateEntity(entity);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
            case MONEY:
            {
                return AdvancedGuilds.getEconomy().depositPlayer(player, price).transactionSuccess();
            }
        }
        return false;
    }
    public int getPlayerBalance(Player player, ShopType shopType) {
        ItemShopPlayerBalanceEntity entity = getPlayerBalanceEntity(player);
        return switch (shopType) {
            case TIME -> entity.getTimeCoins();
            case KILLS -> entity.getKillCoins();
            case MONEY -> (int) AdvancedGuilds.getEconomy().getBalance(player);
        };
    }

    public static void loadAllCategories(ItemShopCategoryRepository categoryRepository) {
        try {
            for (ItemShopCategoryEntity category : categoryRepository.getAllEntities()) {
                cachedCategories.put(category.getName(), category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadAllProducts(ItemShopProductEntityRepository productEntityManager) {
        try {
            for (ItemShopProductEntity product : productEntityManager.getAllEntities()) {
                List<ItemShopProductEntity> orDefault = cachedProducts.getOrDefault(product.getCategory(), new ArrayList<>());
                orDefault.add(product);
                cachedProducts.put(product.getCategory(), orDefault);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadAllPrices(ItemShopProductPriceTypeRepository itemShopProductPriceTypeRepository) {
        try {
            for (ItemShopProductPriceType priceType : itemShopProductPriceTypeRepository.getAllEntities()) {
                ItemShopProductEntity product = priceType.getProduct();
                Map<ShopType, ItemShopProductPriceType> prices = cachedPrices.getOrDefault(product, new HashMap<>());
                prices.put(priceType.getShopType(), priceType);
                cachedPrices.put(product, prices);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
