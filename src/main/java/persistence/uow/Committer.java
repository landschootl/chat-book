package persistence.uow;

import domain.User;

public class Committer extends Visitor {

    @Override
    public void visit(User u) {
        //UserMapper.getInstance().update(u);
    }
}
