package com.zemosolabs.debuggingscenarios;

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CartService implements ICartService{

  private final Map<UUID, Map<Item, Integer>> fCart = new ConcurrentHashMap<>();

  @Override
  public void addItemToCart(final UUID customerId, final Item item) {
    Preconditions.checkNotNull(item, "Item");
    synchronized (customerId){
      var cart = getCart(customerId);
      if(cart.isEmpty()){
        fCart.put(customerId, cart);
      }

     // System.out.println(cart.containsKey(item) + " " + item.getName()); -------> working

      if(cart.containsKey(item)){
        cart.put(item, cart.get(item) + 1);
      } else {
        cart.put(item, 1);
      }

     // System.out.println(cart.get(item) + " " + item.getName());   ------> correct
    }
  }

  @Override
  public void removeItemFromCart(final UUID customerId, final Item item) {
    Preconditions.checkNotNull(item, "Item");
    synchronized (customerId){
      var cart = getCart(customerId);
      Preconditions.checkState(
              cart != null && cart.containsKey(item.getName()),
              "Cart doesn't contain the supplied item.");
      cart.remove(item.getName());
    }
  }

  @Override
  public void clearCart(final UUID customerId) {
    Preconditions.checkNotNull(customerId, "CustomerId");
    fCart.put(customerId, new ConcurrentHashMap<>());
  }

  @Override
  public Map<Item, Integer> getCart(final UUID customerId) {
    Preconditions.checkNotNull(customerId, "CustomerId");
    synchronized (customerId){
      var cart = fCart.get(customerId);
      return cart != null ? cart : new ConcurrentHashMap<>();
    }
  }
}
