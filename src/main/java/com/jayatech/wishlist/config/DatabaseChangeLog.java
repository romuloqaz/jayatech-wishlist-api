package com.jayatech.wishlist.config;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.repository.ProductRepository;
import com.jayatech.wishlist.domain.repository.WishlistRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class DatabaseChangeLog {

    @ChangeSet(order = "001", id = "seed product v1", author = "Romulo")
    public void seedProducts(ProductRepository productRepository) {
        List<Product> products = new ArrayList<>();
        products.add(createProduct("Mouse", BigDecimal.valueOf(15.5), "Mouse óptico com fio"));
        products.add(createProduct("Teclado", BigDecimal.valueOf(25.0), "Teclado mecânico retroiluminado"));
        products.add(createProduct("Monitor", BigDecimal.valueOf(199.99), "Monitor LED de 24 polegadas"));
        products.add(createProduct("Headphones", BigDecimal.valueOf(39.99), "Fones de ouvido com cancelamento de ruído"));
        products.add(createProduct("Smartphone", BigDecimal.valueOf(499.99), "Smartphone Android de última geração"));
        products.add(createProduct("Tablet", BigDecimal.valueOf(199.0), "Tablet de 10 polegadas com caneta stylus"));
        products.add(createProduct("Câmera DSLR", BigDecimal.valueOf(799.0), "Câmera digital DSLR de 24MP"));
        products.add(createProduct("Impressora", BigDecimal.valueOf(129.99), "Impressora a jato de tinta multifuncional"));
        products.add(createProduct("Roteador Wi-Fi", BigDecimal.valueOf(49.95), "Roteador dual-band de alta velocidade"));
        products.add(createProduct("Máquina de Lavar", BigDecimal.valueOf(349.0), "Máquina de lavar roupa de carga frontal"));
        products.add(createProduct("Geladeira", BigDecimal.valueOf(599.0), "Geladeira Frost-Free com dispenser de água"));
        products.add(createProduct("Máquina de Café", BigDecimal.valueOf(79.95), "Máquina de café expresso automática"));
        products.add(createProduct("Forno de Micro-ondas", BigDecimal.valueOf(69.99), "Forno de micro-ondas de 1000W"));
        products.add(createProduct("Batedeira", BigDecimal.valueOf(29.99), "Batedeira elétrica de 300W"));
        products.add(createProduct("Ventilador", BigDecimal.valueOf(19.95), "Ventilador de torre com controle remoto"));
        products.add(createProduct("Cadeira de Escritório", BigDecimal.valueOf(89.0), "Cadeira ergonômica de couro sintético"));
        products.add(createProduct("Mochila", BigDecimal.valueOf(39.95), "Mochila resistente à água para laptop"));
        products.add(createProduct("Relógio Inteligente", BigDecimal.valueOf(129.0), "Relógio inteligente com monitor de frequência cardíaca"));
        products.add(createProduct("Skate Elétrico", BigDecimal.valueOf(299.99), "Skate elétrico com controle remoto"));
        products.add(createProduct("Churrasqueira a Gás", BigDecimal.valueOf(199.0), "Churrasqueira a gás com queimadores ajustáveis"));
        products.add(createProduct("Panela de Pressão", BigDecimal.valueOf(49.95), "Panela de pressão elétrica multifuncional"));
        products.add(createProduct("Violão", BigDecimal.valueOf(149.0), "Violão acústico de madeira maciça"));
        products.add(createProduct("Bicicleta de Montanha", BigDecimal.valueOf(349.0), "Bicicleta de montanha de 21 velocidades"));
        products.add(createProduct("Ferramenta de Jardinagem", BigDecimal.valueOf(29.99), "Conjunto de ferramentas de jardinagem"));
        products.add(createProduct("Cafeteira", BigDecimal.valueOf(39.99), "Cafeteira com jarra de vidro e timer programável"));
        productRepository.insert(products);
    }

    @ChangeSet(order = "002", id = "seed wishlist v1", author = "Romulo")
    public void seedWishlist(WishlistRepository wishlistRepository) {
        wishlistRepository.insert(createWishlist());
    }

    private Product createProduct(String name, BigDecimal price, String description) {
        return Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();
    }

    private Wishlist createWishlist() {
        return  Wishlist.builder()
                .id("wishlistId1")
                .userId("6518691eb39160509735ec5a")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(new ArrayList<>())
                .build();
    }
}