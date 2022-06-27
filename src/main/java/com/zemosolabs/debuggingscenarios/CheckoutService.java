package com.zemosolabs.debuggingscenarios;

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CheckoutService implements ICheckoutService{
  private final ICartService fCartService;
  private final IBalanceService fBalanceService;
  private final IItemCatalogue fItemCatalogue;

  public CheckoutService(
          final ICartService cartService,
          final IBalanceService balanceService,
          final IItemCatalogue itemCatalogue){
    fCartService = Preconditions.checkNotNull(cartService, "CartService");
    fBalanceService = Preconditions.checkNotNull(balanceService, "BalanceService");
    fItemCatalogue = Preconditions.checkNotNull(itemCatalogue, "ItemCatalogue");
  }

  @Override
  public synchronized void checkout(final UUID customerId) {
    Preconditions.checkNotNull(customerId, "CustomerId");
    var cart = fCartService.getCart(customerId);
    Preconditions.checkState(
            cart != null && !cart.isEmpty(),
            "Customer doesn't have items in his cart.");


//      var amount = cart.keySet().stream()
//              .filter(itemKey -> fItemCatalogue.getItem(itemKey.getName()).isPresent())
//              .map(itemKey -> fItemCatalogue.getItem(itemKey.getName()).get().getCost() * cart.get(itemKey))
//              .reduce(0.0D, Double::sum);


      // ----------------added and changed by me ----------------
      var amount = 0D;
      for(Item s  : fCartService.getCart(customerId).keySet()){
        amount += s.getQuantity() * s.getCost();
      }

      System.out.println(amount + " checking amount");   //--------------> getting amount zero here

      fBalanceService.deductBalance(customerId, amount);
      fCartService.clearCart(customerId);
    }

}
