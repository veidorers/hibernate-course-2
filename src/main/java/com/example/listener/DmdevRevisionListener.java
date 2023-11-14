package com.example.listener;

import com.example.entity.Revision;
import org.hibernate.envers.RevisionListener;

public class DmdevRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        ((Revision) revisionEntity).setUsername("dmdev");
    }
}
