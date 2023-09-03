Feature: grep by tag
#  format
#  path or file
#  multiple tag group

  Scenario: no tag on feature and do not create file
    Given a feature at "a.feature":
      """
      # language: zh-CN
      功能: 功能
      """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
      """
      = []
      """

  Scenario: matches one tag
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag
    功能: 功能1
    """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag
               功能: 功能1
               ```
    """

  Scenario: matches two tags
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1 @tag2
    功能: 功能1
    """
    When grep "a.feature" and specify tag: "@tag2,@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1 @tag2
               功能: 功能1
               ```
    """

  Scenario: matches one tag in English
    Given a feature at "a.feature":
    """
    @tag
    Feature: feature1
    """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
    """
    a.feature: ```
               # language: en
               @tag
               Feature: feature1
               ```
    """

  Scenario: output feature with correct format
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1  @tag2
    功能: 功能名字
    第1行描述
    第2行描述
    """
    When grep "a.feature" and specify tag: "@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1 @tag2
               功能: 功能名字
                 第1行描述
                 第2行描述
               ```
    """

#  Rule: selection
