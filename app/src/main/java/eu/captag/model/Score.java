package eu.captag.model;


import com.parse.ParseClassName;
import com.parse.ParseObject;


/**
 * Score domain object.
 * @author Ulrich Raab
 */
@ParseClassName("Score")
public class Score extends ParseObject {


   public static final String ATTRIBUTE_VALUE = "value";

   public static final String POINTER_TAG = "tag";
   public static final String POINTER_TEAM = "team";


   /**
    * Returns the tag related to this score object.
    */
   public Tag getTag () {
      return (Tag) getParseObject(POINTER_TAG);
   }


   /**
    * Returns the team related to this score object.
    */
   public Team getTeam () {
      return (Team) getParseObject(POINTER_TEAM);
   }


   /**
    * Returns the value of this score object.
    */
   public int getValue () {
      return getInt(ATTRIBUTE_VALUE);
   }


   @Override
   public boolean equals (Object o) {

      if (this == o) {
         return true;
      }

      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      Score score = (Score) o;
      return getObjectId().equals(score.getObjectId());
   }


   @Override
   public int hashCode () {
      return getObjectId().hashCode();
   }


   @Override
   public String toString () {
      return Score.class.getSimpleName() + "{objectId=" + getObjectId() + "}";
   }
}
