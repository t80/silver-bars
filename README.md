# Silver bars test for Credit Suisse

#### Decisions & Reasons

- For data types/format for price and quantity to be serialized float/double data formats were avoided to to prevent any issues with lack of precision.

- A concurrent hashmap was used as the data was being written to and read concurrently.

- Interfaces were used to decouple the infrastructure layer and domain

- In the absence of any specific validation requirements a minimal level of obvious validation was added to price & quantity both at the infrastructure layer and the domain layer to cover a scenario where the domain was used by some other client/transport mechanism
 
- Although the storage of the orders could have been harnessed to support the ordering requirements and could "potentially" have been more efficient this seemed like a poor separation of concerns, especially in the absence of performance requirements.  It would also have been presumptuous that this was the only sorting/view of the data that the storage had to cater for.

- The general testing approach was BDD with some subsequent TDD. 