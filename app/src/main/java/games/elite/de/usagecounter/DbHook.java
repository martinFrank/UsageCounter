package games.elite.de.usagecounter;

class DbHook {

    private final DatabaseAdapter databaseAdapter;

    public DbHook(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public void addTimeStamp(int counterId, int delta) {
        databaseAdapter.insertTimeStamp(new TimeStamp(counterId, delta, System.currentTimeMillis()));
    }
}
