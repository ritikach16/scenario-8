package com.zemosolabs.debuggingscenarios;

import java.util.Map;
import java.util.UUID;

public interface ICartService {
  void addItemToCart(UUID customerId, Item item);

  void removeItemFromCart(UUID customerId, Item item);

  void clearCart(UUID customerId);

  Map<Item, Integer> getCart(UUID customerId);
}
