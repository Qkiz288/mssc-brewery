package guru.springframework.msscbrewery.web.model;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
}
