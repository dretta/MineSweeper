package com.daniel.minesweeper;

/**
 * Created by Daniel on 1/12/2015.
 */
public class Session{
    int _id, _difficulty;
    boolean _result;
    float _time, _exploration;

    public Session(){}

    public Session(int id, int difficulty, boolean result, float time, float exploration){
        this._id = id;
        this._difficulty = difficulty;
        this._result = result;
        this._time = time;
        this._exploration = exploration;
    }

    public Session(int id, int difficulty, boolean result, float exploration){
        this._id = id;
        this._difficulty = difficulty;
        this._result = result;
        this._time = 0.0f;
        this._exploration = exploration;
    }

    public int getId(){
        return _id;
    }

    public void setId(int id){
        this._id = id;
    }

    public int getDifficulty() {return _difficulty;}

    public void setDifficulty(int difficulty) {this._difficulty = difficulty;}

    public boolean getResult(){
        return _result;
    }

    public void setResult(boolean result){
        this._result = result;
    }

    public float getTime(){
        return _time;
    }

    public void setTime(float time){
        this._time = time;
    }

    public float getExploration(){
        return _exploration;
    }

    public void setExploration(float exploration){
        this._exploration = exploration;
    }

}
