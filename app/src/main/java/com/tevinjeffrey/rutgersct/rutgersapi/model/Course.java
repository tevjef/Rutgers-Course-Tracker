package com.tevinjeffrey.rutgersct.rutgersapi.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.tevinjeffrey.rutgersct.rutgersapi.utils.SectionUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//This class is a bit complicated. It's a POJO for the GSON library to quickly serialize json data
// into java objects. It is also a parcelable object which means it allows the android system to serialize this object.
// Which in turn allows me to pss this object between Activities and Fragments.
public class Course implements Comparable, Parcelable {

    private String title;
    private String subject;
    private double credits;
    private String courseNumber;
    private String subjectNotes;
    private String courseNotes;
    private String preReqNotes;
    private String offeringUnitCode;
    private int openSections;
    private List<Sections> sections = new ArrayList<>();
    private String expandedTitle;


    public Course() {
    }

    public String getSubject() {
        return subject;
    }

    public String getCredits() {
        if (credits % 1 == 0) {
            return String.valueOf((int) credits);
        } else {
            return String.valueOf(credits);
        }
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getSubjectNotes() {
        return subjectNotes;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public String getPreReqNotes() {
        return preReqNotes;
    }

    public String getOfferingUnitCode() {
        return offeringUnitCode;
    }

    public int getOpenSections() {
        return openSections - getNumberOfOpenNoPrintSections();
    }

    public int getSectionsTotal() {
        return getSections().size() - getNumberOfNoPrintSections();
    }

    String getExpandedTitle() {
        if (expandedTitle != null) {
            return expandedTitle.replaceAll("\\s+", " ").trim();
        }
        return null;
    }

    String getTitle() {
        if (title != null) {
            return title.replaceAll("\\s+", " ").trim();
        }
        return null;
    }

    public String getTrueTitle() {
        return (getExpandedTitle() == null || getExpandedTitle().length() <= 0) ? getTitle() :
                getExpandedTitle();
    }

    public int getNumberOfNoPrintSections() {
        int num = 0;
        for (Sections s : getSections()) {
            if (!s.isPrinted()) ++num;
        }
        return num;
    }

    public int getNumberOfOpenNoPrintSections() {
        int num = 0;
        for (Sections s : getSections()) {
            if (!s.isPrinted() && s.openStatus) ++num;
        }
        return num;
    }

    public List<Sections> getSections() {
        return sections;
    }

    public void setSections(List<Sections> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return getTrueTitle();
    }

    @Override
    public int compareTo(@NonNull Object another) {
        Course b = (Course) another;
        if (Integer.valueOf(this.getSubject()) > Integer.valueOf(b.getSubject())) {
            return 1;
        } else if (Integer.valueOf(this.getSubject()) < Integer.valueOf(b.getSubject())) {
            return -1;
        } else if (Integer.valueOf(this.getSubject()).equals(Integer.valueOf(b.getSubject()))) {
            if (Integer.valueOf(this.getCourseNumber()) > Integer.valueOf(b.getCourseNumber())) {
                return 1;
            } else if (Integer.valueOf(this.getCourseNumber()) < Integer.valueOf(b.getCourseNumber())) {
                return -1;
            } else if (Integer.valueOf(this.getCourseNumber()).equals(Integer.valueOf(b.getCourseNumber()))) {
                if (Integer.valueOf(getSections().get(0).getIndex()) > Integer.valueOf(b.getSections().get(0).getIndex())) {
                    return 1;
                } else if (Integer.valueOf(getSections().get(0).getIndex()) < Integer.valueOf(b.getSections().get(0).getIndex())) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static class Sections implements Parcelable {

        List<Instructors> instructors = new ArrayList<>();
        List<MeetingTimes> meetingTimes = new ArrayList<>();
        List<CrossListedSections> crossListedSections = new ArrayList<>();
        List<Majors> majors = new ArrayList<>();
        List<Comments> comments = new ArrayList<>();
        String subtitle;
        String index;
        String specialPermissionAddCodeDescription;
        String specialPermissionAddCode;
        String specialPermissionDropCode;
        String offeringUnitCode;
        String synopsisUrl;
        String examCode;
        String sectionNotes;
        String number;
        String campusCode;
        String printed;
        int stopPoint;
        boolean openStatus;

        public Sections() {
            
        }

        public List<MeetingTimes> getMeetingTimes() {
            return meetingTimes;
        }

        public List<Instructors> getInstructors() {
            return instructors;
        }

        public List<Comments> getComments() {
            return comments;
        }

        public List<Majors> getMajors() {
            return majors;
        }

        public List<CrossListedSections> getCrossListedSections() {
            return crossListedSections;
        }

        public String getToStringMeetingTimes(String joiner) {
            return StringUtils.join(meetingTimes, joiner);
        }

        public String getToStringInstructors(String joiner) {
            return StringUtils.join(instructors, joiner);
        }

        public String getToStringComments(String joiner) {
            return StringUtils.join(comments, joiner);
        }

        public String getToStringMajors(String joiner) {
            return StringUtils.join(majors, joiner);
        }

        public String getToStringCrossListedSections(String joiner) {
            return StringUtils.join(instructors, joiner);
        }

        public int getStopPoint() {
            return stopPoint;
        }

        public String getIndex() {
            return index;
        }

        public String getSpecialPermissionAddCodeDescription() {
            return specialPermissionAddCodeDescription;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getSpecialPermissionAddCode() {
            return specialPermissionAddCode;
        }

        public String getSpecialPermissionDropCode() {
            return specialPermissionDropCode;
        }

        public String getOfferingUnitCode() {
            return offeringUnitCode;
        }

        public String getSynopsisUrl() {
            return synopsisUrl;
        }

        public String getExamCode() {
            return examCode;
        }

        public String getSectionNotes() {
            return sectionNotes;
        }

        public void setSectionNotes(String sectionNotes) {
            this.sectionNotes = sectionNotes;
        }

        public String getNumber() {
            return number;
        }

        public String getCampusCode() {
            return campusCode;
        }

        public boolean isOpenStatus() {
            return openStatus;
        }

        public boolean hasNotes() {
            return getSectionNotes() != null;
        }

        public boolean hasComments() {
            return getComments().size() > 0;
        }

        public boolean hasMajors() {
            return getMajors().size() > 0;
        }

        public boolean hasSpecialPermission() {
            return getSpecialPermissionAddCode() != null;
        }

        public boolean hasSubtitle() {
            return getSubtitle() != null;
        }

        public boolean hasCrossListed() {
            return getCrossListedSections().size() > 0;
        }

        public String getPrinted() {
            return printed;
        }

        public boolean isPrinted() {
            return getPrinted().equals("Y");
        }

        public boolean hasMetaData() {
            return hasCrossListed() ||
                    hasSpecialPermission() ||
                    hasSubtitle() ||
                    hasMajors() ||
                    hasComments() ||
                    hasNotes();
        }

        @Override
        public String toString() {
            return "Section #" + getNumber();
        }


        public static class MeetingTimes implements Comparable, Parcelable {

            String meetingDay;
            String roomNumber;
            String pmCode;
            String startTime;
            String endTime;
            String buildingCode;
            String meetingModeDesc;
            String meetingModeCode;
            String campusAbbrev;
            String baClassHours;

            public MeetingTimes() {
                
            }

            public boolean isLecture() {
                return getMeetingModeCode() != null && getMeetingModeCode().equals("02");
            }

            public boolean isRecitation() {
                return getMeetingModeCode() != null && getMeetingModeCode().equals("03");
            }

            public boolean isLab() {
                return getMeetingModeCode() != null && getMeetingModeCode().equals("05");
            }

            public boolean isStudio() {
                return getMeetingModeCode() != null && getMeetingModeCode().equals("07");
            }

            public boolean isByArrangement() {
                return getBaClassHours() != null && getBaClassHours().equals("B");
            }

            public String getStartTime() {
                return startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public String getBuildingCode() {
                return buildingCode;
            }

            public String getMeetingModeDesc() {
                return meetingModeDesc;
            }

            public String getMeetingModeCode() {
                return meetingModeCode;
            }

            public String getCampusAbbrev() {
                return campusAbbrev;
            }

            public String getCampus() {
                String campus;

                if (getCampusAbbrev() != null) {
                    switch (getCampusAbbrev()) {
                        case "NWK":
                            campus = "Newark";
                            break;
                        case "CAM":
                            campus = "Camden";
                            break;
                        default:
                            campus = "New Brunswick";
                    }
                    return campus;
                }
                return null;
            }


            public String getBaClassHours() {
                return baClassHours;
            }

            public String getMeetingDay() {
                return meetingDay;
            }

            public String getRoomNumber() {
                return roomNumber;
            }

            public String getPmCode() {
                return pmCode;
            }

            @Override
            public int compareTo(@NonNull Object time) {
                MeetingTimes b = (MeetingTimes) time;
                int result = new ClassRankComparator().compare(this, b);
                if (result == 0) result = new TimeRankComparator().compare(this, b);
                return result;
            }

            private class ClassRankComparator implements Comparator<MeetingTimes> {
                @Override
                public int compare(MeetingTimes lhs, MeetingTimes rhs) {
                    if (SectionUtils.getClassRank(lhs) < SectionUtils.getClassRank(rhs)) {
                        return 1;
                    } else if (SectionUtils.getClassRank(lhs) > SectionUtils.getClassRank(rhs)){
                        return -1;
                    }
                    return 0;
                }
            }

            private class TimeRankComparator implements Comparator<MeetingTimes> {
                @Override
                public int compare(MeetingTimes lhs, MeetingTimes rhs) {
                    if (SectionUtils.getTimeRank(lhs) < SectionUtils.getTimeRank(rhs)) {
                        return 1;
                    } else if (SectionUtils.getTimeRank(lhs) > SectionUtils.getTimeRank(rhs)) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.meetingDay);
                dest.writeString(this.roomNumber);
                dest.writeString(this.pmCode);
                dest.writeString(this.startTime);
                dest.writeString(this.endTime);
                dest.writeString(this.buildingCode);
                dest.writeString(this.meetingModeDesc);
                dest.writeString(this.meetingModeCode);
                dest.writeString(this.campusAbbrev);
                dest.writeString(this.baClassHours);
            }

            protected MeetingTimes(Parcel in) {
                this.meetingDay = in.readString();
                this.roomNumber = in.readString();
                this.pmCode = in.readString();
                this.startTime = in.readString();
                this.endTime = in.readString();
                this.buildingCode = in.readString();
                this.meetingModeDesc = in.readString();
                this.meetingModeCode = in.readString();
                this.campusAbbrev = in.readString();
                this.baClassHours = in.readString();
            }

            public static final Creator<MeetingTimes> CREATOR = new Creator<MeetingTimes>() {
                public MeetingTimes createFromParcel(Parcel source) {
                    return new MeetingTimes(source);
                }

                public MeetingTimes[] newArray(int size) {
                    return new MeetingTimes[size];
                }
            };

            @Override
            public String toString() {
                return SectionUtils.getMeetingDayName(this) + " " + SectionUtils.getMeetingHours(this) + " "
                        + SectionUtils.getClassLocation(this);
            }
        }

        public static class Instructors implements Parcelable {

            String name;

            public Instructors() {
                
            }

            public String getName() {
                return name;
            }

            public String getFirstName() {
                String firstName = StringUtils.substringAfter(name, ",");
                if (name.equals(firstName)) {
                    return "";
                } else {
                    return firstName.trim();
                }
            }

            public String getLastName() {
                String lastName = StringUtils.substringBefore(name, ",");
                return lastName;
            }

            @Override
            public String toString() {
                return getName();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
            }

            protected Instructors(Parcel in) {
                this.name = in.readString();
            }

            public static final Creator<Instructors> CREATOR = new Creator<Instructors>() {
                public Instructors createFromParcel(Parcel source) {
                    return new Instructors(source);
                }

                public Instructors[] newArray(int size) {
                    return new Instructors[size];
                }
            };
        }

        public static class Comments implements Parcelable {


            String description;

            public Comments() {
                
            }

            @Override
            public String toString() {
                return getDescription();
            }

            public String getDescription() {
                return description;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.description);
            }

            protected Comments(Parcel in) {
                this.description = in.readString();
            }

            public static final Creator<Comments> CREATOR = new Creator<Comments>() {
                public Comments createFromParcel(Parcel source) {
                    return new Comments(source);
                }

                public Comments[] newArray(int size) {
                    return new Comments[size];
                }
            };
        }

        public static class CrossListedSections implements Parcelable {

            String sectionNumber;
            String offeringUnitCode;
            String subjectCode;
            String courseNumber;

            public CrossListedSections() {
                
            }


            public String getSectionNumber() {
                return sectionNumber;
            }

            public String getOfferingUnitCode() {
                return offeringUnitCode;
            }

            public String getSubjectCode() {
                return subjectCode;
            }

            public String getCourseNumber() {
                return courseNumber;
            }

            public String getFullCrossListedSection() {
                return getOfferingUnitCode() + ":" + getSubjectCode() + ":"
                        + getCourseNumber() + ":" + getSectionNumber();
            }

            @Override
            public String toString() {
                return getFullCrossListedSection();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.sectionNumber);
                dest.writeString(this.offeringUnitCode);
                dest.writeString(this.subjectCode);
                dest.writeString(this.courseNumber);
            }

            protected CrossListedSections(Parcel in) {
                this.sectionNumber = in.readString();
                this.offeringUnitCode = in.readString();
                this.subjectCode = in.readString();
                this.courseNumber = in.readString();
            }

            public static final Creator<CrossListedSections> CREATOR = new Creator<CrossListedSections>() {
                public CrossListedSections createFromParcel(Parcel source) {
                    return new CrossListedSections(source);
                }

                public CrossListedSections[] newArray(int size) {
                    return new CrossListedSections[size];
                }
            };
        }

        public static class Majors implements Parcelable {

            boolean isMajorCode;
            boolean isUnitCode;
            String code;

            public Majors() {
            }


            public boolean isMajorCode() {
                return isMajorCode;
            }

            public boolean isUnitCode() {
                return isUnitCode;
            }

            public String getCode() {
                return code;
            }

            @Override
            public String toString() {
                return getCode();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeByte(isMajorCode ? (byte) 1 : (byte) 0);
                dest.writeByte(isUnitCode ? (byte) 1 : (byte) 0);
                dest.writeString(this.code);
            }

            protected Majors(Parcel in) {
                this.isMajorCode = in.readByte() != 0;
                this.isUnitCode = in.readByte() != 0;
                this.code = in.readString();
            }

            public static final Creator<Majors> CREATOR = new Creator<Majors>() {
                public Majors createFromParcel(Parcel source) {
                    return new Majors(source);
                }

                public Majors[] newArray(int size) {
                    return new Majors[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(this.instructors);
            dest.writeTypedList(this.meetingTimes);
            dest.writeTypedList(this.crossListedSections);
            dest.writeTypedList(this.majors);
            dest.writeTypedList(this.comments);
            dest.writeString(this.subtitle);
            dest.writeString(this.index);
            dest.writeString(this.specialPermissionAddCodeDescription);
            dest.writeString(this.specialPermissionAddCode);
            dest.writeString(this.specialPermissionDropCode);
            dest.writeString(this.offeringUnitCode);
            dest.writeString(this.synopsisUrl);
            dest.writeString(this.examCode);
            dest.writeString(this.sectionNotes);
            dest.writeString(this.number);
            dest.writeString(this.campusCode);
            dest.writeString(this.printed);
            dest.writeInt(this.stopPoint);
            dest.writeByte(openStatus ? (byte) 1 : (byte) 0);
        }

        protected Sections(Parcel in) {
            this.instructors = new ArrayList<Instructors>();
            in.readTypedList(instructors, Instructors.CREATOR);
            this.meetingTimes = new ArrayList<MeetingTimes>();
            in.readTypedList(meetingTimes, MeetingTimes.CREATOR);
            this.crossListedSections = new ArrayList<CrossListedSections>();
            in.readTypedList(crossListedSections, CrossListedSections.CREATOR);
            this.majors = new ArrayList<Majors>();
            in.readTypedList(majors, Majors.CREATOR);
            this.comments = new ArrayList<Comments>();
            in.readTypedList(comments, Comments.CREATOR);
            this.subtitle = in.readString();
            this.index = in.readString();
            this.specialPermissionAddCodeDescription = in.readString();
            this.specialPermissionAddCode = in.readString();
            this.specialPermissionDropCode = in.readString();
            this.offeringUnitCode = in.readString();
            this.synopsisUrl = in.readString();
            this.examCode = in.readString();
            this.sectionNotes = in.readString();
            this.number = in.readString();
            this.campusCode = in.readString();
            this.printed = in.readString();
            this.stopPoint = in.readInt();
            this.openStatus = in.readByte() != 0;
        }

        public static final Creator<Sections> CREATOR = new Creator<Sections>() {
            public Sections createFromParcel(Parcel source) {
                return new Sections(source);
            }

            public Sections[] newArray(int size) {
                return new Sections[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.subject);
        dest.writeDouble(this.credits);
        dest.writeString(this.courseNumber);
        dest.writeString(this.subjectNotes);
        dest.writeString(this.courseNotes);
        dest.writeString(this.preReqNotes);
        dest.writeString(this.offeringUnitCode);
        dest.writeInt(this.openSections);
        dest.writeTypedList(this.sections);
        dest.writeString(this.expandedTitle);
    }

    protected Course(Parcel in) {
        this.title = in.readString();
        this.subject = in.readString();
        this.credits = in.readDouble();
        this.courseNumber = in.readString();
        this.subjectNotes = in.readString();
        this.courseNotes = in.readString();
        this.preReqNotes = in.readString();
        this.offeringUnitCode = in.readString();
        this.openSections = in.readInt();
        this.sections = new ArrayList<Sections>();
        in.readTypedList(sections, Sections.CREATOR);
        this.expandedTitle = in.readString();
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}