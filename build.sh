#!/bin/bash

# 设置变量
SRC_DIR=src/main/java
RESOURCES_DIR=src/main/resources
OUTPUT_DIR=build
CLASSES_DIR=$OUTPUT_DIR/classes
JAR_NAME=miniradar-1.0.0.jar

# 创建输出目录
mkdir -p $OUTPUT_DIR
mkdir -p $CLASSES_DIR

# 编译Java源代码
echo "Compiling Java source files..."
javac -d $CLASSES_DIR -source 17 -target 17 -cp "$CLASSES_DIR" $(find $SRC_DIR -name "*.java")

if [ $? -ne 0 ]; then
    echo "Error: Compilation failed"
    exit 1
fi

# 检查编译结果
if [ ! -d "$CLASSES_DIR/com/miniradar" ]; then
    echo "Error: Compiled classes not found"
    exit 1
fi

# 创建jar文件，包含类文件和资源文件
echo "Creating JAR file..."
jar cvf $OUTPUT_DIR/$JAR_NAME -C $CLASSES_DIR . -C $RESOURCES_DIR .

# 显示构建结果
echo "Build completed successfully!"
echo "Jar file created: $OUTPUT_DIR/$JAR_NAME"

# 检查文件大小
if [ -f "$OUTPUT_DIR/$JAR_NAME" ]; then
    FILE_SIZE=$(ls -lh $OUTPUT_DIR/$JAR_NAME | awk '{print $5}')
    echo "File size: $FILE_SIZE"
else
    echo "Error: Jar file not created"
    exit 1
fi

# 验证模组元数据文件
if [ -f "$RESOURCES_DIR/fabric.mod.json" ]; then
    echo "\nVerifying fabric.mod.json..."
    cat $RESOURCES_DIR/fabric.mod.json
else
    echo "Error: fabric.mod.json not found"
    exit 1
fi

# 验证JAR文件内容
echo "\nVerifying JAR file contents..."
jar tf $OUTPUT_DIR/$JAR_NAME
