import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Coordinate {
    private double latitude;
    private double longitude;

    public boolean isNullIsLand() {
        return (latitude == 0.0) && (longitude == 0.0);
    }
}
