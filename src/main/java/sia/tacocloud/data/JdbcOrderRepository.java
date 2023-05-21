package sia.tacocloud.data;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sia.tacocloud.models.Ingredient;
import sia.tacocloud.models.IngredientRef;
import sia.tacocloud.models.Taco;
import sia.tacocloud.models.TacoOrder;

@Repository
public class JdbcOrderRepository implements OrderRepository {

	private final JdbcOperations jdbcOperations;

	public JdbcOrderRepository(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	@Override
	@Transactional
	public TacoOrder save(TacoOrder order) {
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				"insert into Taco_Order (delivery_name, delivery_street, delivery_city, delivery_state, delivery_zip, cc_number, cc_expiration, cc_cvv, placed_at) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.TIMESTAMP);
		pscf.setReturnGeneratedKeys(true);
		order.setPlacedAt(new Date());
		PreparedStatementCreator psc = pscf.newPreparedStatementCreator(new Object[] { order.getDeliveryName(),
				order.getDeliveryStreet(), order.getDeliveryCity(), order.getDeliveryState(), order.getDeliveryZip(),
				order.getCcNumber(), order.getCcExpiration(), order.getCcCVV(), order.getPlacedAt() });
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOperations.update(psc, keyHolder);
		long orderId = keyHolder.getKey().longValue();
		order.setId(orderId);
		List<Taco> tacos = order.getTacos();
		int i = 0;
		for (Taco taco : tacos) {
			saveTaco(orderId, i++, taco);
		}
		return order;

	}
	// create table if not exists Taco (
	// id identity,
	// name varchar(50) not null,
	// taco_order bigint not null,
	// taco_order_key bigint not null,
	// created_at timestamp not null
	// );

	private long saveTaco(long orderId, int orderKey, Taco taco) {
		taco.setCreatedAt(new Date());
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				"insert into Taco (name, taco_order, taco_order_key,created_at) values (?, ?, ?, ?)", Types.VARCHAR,
				Types.BIGINT, Types.BIGINT, Types.TIMESTAMP);
		pscf.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = pscf
			.newPreparedStatementCreator(new Object[] { taco.getName(), orderId, orderKey, taco.getCreatedAt() });
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        // Convert list of Ingredient to list of IngredientRef
        // Using IngredientRefByIngredient converter
        List<IngredientRef> ingredientRefs = taco.getIngredients().stream()
            .map(ingredient -> new IngredientRef(ingredient.getId()))
            .collect(java.util.stream.Collectors.toList());

        saveIngredientRef(tacoId, ingredientRefs);
        return tacoId;
	}
    // create table if not exists Ingredient_Ref (
    //     ingredient varchar(4) not null,
    //     taco bigint not null,
    //     taco_key bigint not null
    // );
    private void saveIngredientRef(long tacoId, List<IngredientRef> ingredientRefs) {
        int key = 0;
        for (IngredientRef ingredientRef : ingredientRefs) {
            jdbcOperations.update("insert into Ingredient_Ref (ingredient, taco, taco_key) values (?, ?, ?)",
            ingredientRef.getIngredient(), tacoId, key++);
        }
    }

}
