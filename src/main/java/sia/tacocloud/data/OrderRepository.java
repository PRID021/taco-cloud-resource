package sia.tacocloud.data;

import sia.tacocloud.models.TacoOrder;

public interface  OrderRepository {
    TacoOrder save(TacoOrder order);
}
