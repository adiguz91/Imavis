package com.drone.imavis.data.model;

import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by adigu on 06.05.2017.
 */

public class Projects {

//    {
//        "count": 1,
//            "next": null,
//            "previous": null,
//            "results": [
//            {
//                "id": 2,
//                    "tasks": [
//                7,
//                        6,
//                        5
//                ],
//                "created_at": "2016-12-07T02:09:28.515319Z",
//                    "name": "Test",
//                    "description": ""
//            }
//        ]
//    }

    private int count;
    private int next;
    private int previous;
    private List<Project> results;

    private Projects() {}

    public int getCount() {
        return count;
    }

    private void setCount(int count) {
        this.count = count;
    }

    public int getNext() {
        return next;
    }

    private void setNext(int next) {
        this.next = next;
    }

    public int getPrevious() {
        return previous;
    }

    private void setPrevious(int previous) {
        this.previous = previous;
    }

    public List<Project> getResults() {
        return results;
    }

    private void setResults(List<Project> results) {
        this.results = results;
    }
}
