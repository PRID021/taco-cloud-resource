package sia.tacocloud.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.oss.driver.api.core.uuid.Uuids;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;



// import jakarta.persistence.Id;
// import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
// @Entity
@Table("tacos")
public class Taco {
	// @Id
	// @GeneratedValue(strategy =  GenerationType.AUTO)
	@PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED) // define the partition key
	private  UUID id = Uuids.timeBased();

	@NotNull
	@Size(min = 5, message = "Name must at least 5 characters long ")
	private String name;

	@PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING) // define the clustering key
	private Date createdAt = new Date();


	@NotNull
	@Size(min = 1, message = "You must choose at least 1 ingredient")
	@Column("ingredients") // maps the ingredients property to the ingredients column.
	// @ManyToMany()
	private List<IngredientUDT> ingredients = new ArrayList<>();


	public void addIngredient(Ingredient ingredient) {
		this.ingredients.add(TacoUDRUtils.toIngredientUDT(ingredient));
	}

}
