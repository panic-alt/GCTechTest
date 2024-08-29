package com.gc.services.subscription.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@Table(name = "news_categories")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class NewsCategory {

    @Id
    @Column
    private Long id;

    @Column(nullable = false)
    private String name;
}
