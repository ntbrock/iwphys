class UserConfiguration < ActiveRecord::Base
public
  attr_accessible :email_confirmation, :email_sender

  def self.get
    begin
      u = self.find(1)
    rescue
      c =  self.new
    end
  end

  def initialize(parameters = nil)
    super
    self.email_confirmation = 1
    self.email_sender = ''
  end
end
