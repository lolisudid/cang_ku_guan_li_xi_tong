package com.warehouse.service;

import com.warehouse.dao.GoodsDao;
import com.warehouse.entity.Goods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class GoodsService {
  private final GoodsDao goodsDao;

  public GoodsService(GoodsDao goodsDao) { this.goodsDao = goodsDao; }

  public List<Goods> list(String name, int page, int size) {
    int offset = page * size;
    if (name != null && !name.isBlank()) {
      return goodsDao.findByName(name, offset, size);
    }
    return goodsDao.findAll(offset, size);
  }

  public int count(String name) {
    if (name != null && !name.isBlank()) {
      return goodsDao.countByName(name);
    }
    return goodsDao.countAll();
  }

  public Goods getById(String id) {
    Goods g = goodsDao.findById(id);
    if (g == null) throw new RuntimeException("商品不存在: " + id);
    return g;
  }

  @Transactional
  public Goods create(Goods goods) {
    if (goodsDao.existsById(goods.getId())) {
      throw new RuntimeException("商品编号已存在: " + goods.getId());
    }
    goodsDao.insert(goods);
    return goods;
  }

  @Transactional
  public Goods update(String id, Goods goods) {
    Goods existing = getById(id);
    existing.setName(goods.getName());
    existing.setSpec(goods.getSpec());
    existing.setUnit(goods.getUnit());
    existing.setPrice(goods.getPrice());
    existing.setThreshold(goods.getThreshold());
    goodsDao.update(existing);
    return existing;
  }

  @Transactional
  public void delete(String id) {
    if (!goodsDao.existsById(id)) {
      throw new RuntimeException("商品不存在: " + id);
    }
    goodsDao.deleteById(id);
  }
}
