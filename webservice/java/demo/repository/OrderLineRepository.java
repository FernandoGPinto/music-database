package demo.repository;

import demo.model.OrderLine;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Fernando on 01/11/2017.
 */
public interface OrderLineRepository extends CrudRepository<OrderLine, Long> {


}
