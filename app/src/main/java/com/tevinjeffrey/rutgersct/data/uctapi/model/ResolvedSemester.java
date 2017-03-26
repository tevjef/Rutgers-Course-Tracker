// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: model.proto at 109:1
package com.tevinjeffrey.rutgersct.data.uctapi.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.squareup.wire.AndroidMessage;
import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

public final class ResolvedSemester
    extends AndroidMessage<ResolvedSemester, ResolvedSemester.Builder> {
  public static final ProtoAdapter<ResolvedSemester> ADAPTER = new ProtoAdapter_ResolvedSemester();

  public static final Parcelable.Creator<ResolvedSemester> CREATOR =
      AndroidMessage.newCreator(ADAPTER);

  private static final long serialVersionUID = 0L;

  @WireField(
      tag = 1,
      adapter = "com.tevinjeffrey.rutgersct.data.uctapi.model.Semester#ADAPTER"
  )
  @Nullable
  public final Semester current;

  @WireField(
      tag = 2,
      adapter = "com.tevinjeffrey.rutgersct.data.uctapi.model.Semester#ADAPTER"
  )
  @Nullable
  public final Semester last;

  @WireField(
      tag = 3,
      adapter = "com.tevinjeffrey.rutgersct.data.uctapi.model.Semester#ADAPTER"
  )
  @Nullable
  public final Semester next;

  public ResolvedSemester(
      @Nullable Semester current,
      @Nullable Semester last,
      @Nullable Semester next) {
    this(current, last, next, ByteString.EMPTY);
  }

  public ResolvedSemester(
      @Nullable Semester current,
      @Nullable Semester last,
      @Nullable Semester next,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.current = current;
    this.last = last;
    this.next = next;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.current = current;
    builder.last = last;
    builder.next = next;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof ResolvedSemester)) {
      return false;
    }
    ResolvedSemester o = (ResolvedSemester) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(current, o.current)
        && Internal.equals(last, o.last)
        && Internal.equals(next, o.next);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (current != null ? current.hashCode() : 0);
      result = result * 37 + (last != null ? last.hashCode() : 0);
      result = result * 37 + (next != null ? next.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (current != null) {
      builder.append(", current=").append(current);
    }
    if (last != null) {
      builder.append(", last=").append(last);
    }
    if (next != null) {
      builder.append(", next=").append(next);
    }
    return builder.replace(0, 2, "ResolvedSemester{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<ResolvedSemester, Builder> {
    public Semester current;

    public Semester last;

    public Semester next;

    public Builder() {
    }

    public Builder current(Semester current) {
      this.current = current;
      return this;
    }

    public Builder last(Semester last) {
      this.last = last;
      return this;
    }

    public Builder next(Semester next) {
      this.next = next;
      return this;
    }

    @Override
    public ResolvedSemester build() {
      return new ResolvedSemester(current, last, next, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_ResolvedSemester extends ProtoAdapter<ResolvedSemester> {
    ProtoAdapter_ResolvedSemester() {
      super(FieldEncoding.LENGTH_DELIMITED, ResolvedSemester.class);
    }

    @Override
    public int encodedSize(ResolvedSemester value) {
      return (value.current != null ? Semester.ADAPTER.encodedSizeWithTag(1, value.current) : 0)
          + (value.last != null ? Semester.ADAPTER.encodedSizeWithTag(2, value.last) : 0)
          + (value.next != null ? Semester.ADAPTER.encodedSizeWithTag(3, value.next) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, ResolvedSemester value) throws IOException {
      if (value.current != null) {
        Semester.ADAPTER.encodeWithTag(writer, 1, value.current);
      }
      if (value.last != null) {
        Semester.ADAPTER.encodeWithTag(writer, 2, value.last);
      }
      if (value.next != null) {
        Semester.ADAPTER.encodeWithTag(writer, 3, value.next);
      }
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public ResolvedSemester decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1; ) {
        switch (tag) {
          case 1:
            builder.current(Semester.ADAPTER.decode(reader));
            break;
          case 2:
            builder.last(Semester.ADAPTER.decode(reader));
            break;
          case 3:
            builder.next(Semester.ADAPTER.decode(reader));
            break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public ResolvedSemester redact(ResolvedSemester value) {
      Builder builder = value.newBuilder();
      if (builder.current != null) {
        builder.current = Semester.ADAPTER.redact(builder.current);
      }
      if (builder.last != null) {
        builder.last = Semester.ADAPTER.redact(builder.last);
      }
      if (builder.next != null) {
        builder.next = Semester.ADAPTER.redact(builder.next);
      }
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
