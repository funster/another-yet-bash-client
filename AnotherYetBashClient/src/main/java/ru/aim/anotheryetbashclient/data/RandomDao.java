package ru.aim.anotheryetbashclient.data;

public class RandomDao extends AbsSqlDao {

    @Override
    public String getTableName() {
        return "RANDOM_TABLE";
    }
}
