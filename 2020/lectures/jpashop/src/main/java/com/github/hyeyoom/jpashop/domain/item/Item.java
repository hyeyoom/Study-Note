package com.github.hyeyoom.jpashop.domain.item;

import com.github.hyeyoom.jpashop.domain.Category;
import com.github.hyeyoom.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    public void addStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    public void minusStock(int stockQuantity) {
        final int rest = this.stockQuantity - stockQuantity;
        if (rest < 0) {
            throw new NotEnoughStockException("._.");
        }
        this.stockQuantity = rest;
    }
}
