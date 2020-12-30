package com.github.yangwk.weboot.test.invoke;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.util.StreamUtils;

public class InvokeMetadataReqParamsDto {
	public static enum Type {
		Date("yyyyMMdd"), Datetime("yyyyMMddHHmmss"), String(null), Integer(null), File(null), FileByteArray(null);

		private String pattern;

		private Type(String pattern) {
			this.pattern = pattern;
		}
	}

	private String type;
	private String value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Object toRuntimeType() {
		if (Type.Date.name().equalsIgnoreCase(this.type)) {
			return Date.from(LocalDate.parse(this.value, DateTimeFormatter.ofPattern(Type.Date.pattern)).atStartOfDay(ZoneId.systemDefault()).toInstant());
		} else if (Type.Datetime.name().equalsIgnoreCase(this.type)) {
			return Date.from(LocalDateTime.parse(this.value, DateTimeFormatter.ofPattern(Type.Datetime.pattern)).atZone(ZoneId.systemDefault()).toInstant());
		} else if (Type.String.name().equalsIgnoreCase(this.type)) {
			return this.value;
		} else if (Type.Integer.name().equalsIgnoreCase(this.type)) {
			return Integer.valueOf(this.value);
		} else if (Type.File.name().equalsIgnoreCase(this.type)) {
			return new File(this.value);
		} else if (Type.FileByteArray.name().equalsIgnoreCase(this.type)) {
			try (FileInputStream input = new FileInputStream(new File(this.value))) {
				return StreamUtils.copyToByteArray(input);
			} catch (IOException e) {
				throw new RuntimeException("File byte[] parse error", e);
			}
		}

		return null;
	}

}
